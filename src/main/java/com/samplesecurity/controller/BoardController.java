package com.samplesecurity.controller;

import com.samplesecurity.domain.board.AttachFile;
import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.board.Category;
import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.Board.*;
import com.samplesecurity.dto.PageMaker;
import com.samplesecurity.repository.board.CategoryRepository;
import com.samplesecurity.repository.MemberRepository;
import com.samplesecurity.repository.board.AttachFileRepository;
import com.samplesecurity.repository.board.UploadFileRepository;
import com.samplesecurity.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static java.lang.Long.*;

@Controller
@Slf4j
@RequestMapping("/jinsang")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final AttachFileRepository attachFileRepository;

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/jslist")
    public String list(@ModelAttribute("pageDto") BoardPageDto boardPageDto, Model model) {
        log.info("ss" + boardPageDto);
        Pageable pageable = boardPageDto.makePageable();

        Page<BoardListDto> boards = boardService.getBoardList(boardPageDto.getBoardType(), pageable);

        model.addAttribute("boards", new PageMaker<>(boards));

        return "jinsang/jslist";
    }

    @GetMapping("/{id}/{boardType}")
    public String get(@PathVariable("id") Long id,
                      @PathVariable("boardType") String type,
                      @ModelAttribute("pageDto") BoardPageDto boardPageDto,
                      Model model) {
        Board findBoard = boardService.getBoard(id);
        boardPageDto.setBoardType(type);

        model.addAttribute("board", findBoard);

        Long preBoardId = boardService.getPreBoard(id, boardPageDto.getBoardType());

        Board findPreBoard = boardService.getBoard(preBoardId);
        model.addAttribute("preBoard", findPreBoard);


        Long nextBoardId = boardService.getNextBoard(id, boardPageDto.getBoardType());

        Board nextBoard = boardService.getBoard(nextBoardId);
        model.addAttribute("nextBoard", nextBoard);

        return "jinsang/jsview";
    }

    @GetMapping("/write")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String register(@ModelAttribute("pageDto") BoardPageDto boardPageDto) {
        log.info("register pageDto: " + boardPageDto);
        return "jinsang/write";
    }

    @PostMapping("/write")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String register(BoardRegisterDto boardRegisterDto,
                           Authentication authentication,
                           BoardPageDto boardPageDto) {
        log.info("boardDto: " + boardRegisterDto);
        Member findMember = findMember(authentication);
        Category category = categoryRepository.findById(parseLong(boardRegisterDto.getCategory())).get();
        Board board;
        if (boardRegisterDto.getBoardType().equals("2")) {
            board = boardRegisterDto.toEntity(findMember, category);
        } else {
            board = boardRegisterDto.toEntityOfKnow(findMember, category);
        }

        List<AttachFileDto> fileDtos = boardRegisterDto.getFileDtos();

        boardService.register(board, fileDtos);

        return "redirect:/jinsang/jslist"+boardPageDto.getListLink();
    }

    @GetMapping("/modify/{boardId}")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String modifyForm(@PathVariable("boardId") Long boardId,
                             Authentication authentication,
                             @ModelAttribute("pageDto") BoardPageDto boardPageDto,
                             Model model) {
        log.info("modify pageDto: " + boardPageDto);
        Board findBoard = boardService.getBoardByLoginCheck(boardId, authentication);

        model.addAttribute("board", findBoard);
        model.addAttribute("categoryList", categoryRepository.findAll());
        model.addAttribute("files", attachFileRepository.findAllByBoardId(boardId));

        return "jinsang/modifyForm";
    }

    @PostMapping("/modify/{boardId}")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String modify(@PathVariable("boardId") Long boardId,
                         BoardUpdateDto boardUpdateDto,
                         Authentication authentication,
                         BoardPageDto boardPageDto) {
        log.info("modify boardUpdateDto: " + boardUpdateDto);
        log.info("modify boardPageDto boarType: "+boardPageDto.getBoardType()); // 수정시 boardType에 ,가 생기는 문제 ex) 3 -> 3,3
        Member findMember = findMember(authentication);
        String[] type = boardPageDto.getBoardType().split("");
        boardPageDto.setBoardType(type[0]);

        boardService.update(findMember, boardId, boardUpdateDto);

        return "redirect:/jinsang/jslist" + boardPageDto.getListLink();
    }

    @PostMapping("/delete/{boardId}")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String delete(@PathVariable("boardId") Long boardId,
                         BoardPageDto boardPageDto,
                         Authentication authentication) {
        Member findMember = findMember(authentication);
        boardService.delete(boardId, findMember);

        return "redirect:/jinsang/jslist"+boardPageDto.getListLink();
    }

    @GetMapping("/getAttachList")
    @ResponseBody
    public ResponseEntity<List<AttachFile>> getAttachList(Long boardId) {
        log.info("boardId: " + boardId);
        List<AttachFile> boardAttachList = attachFileRepository.findByBoardId(boardId);

        return new ResponseEntity<>(boardAttachList, HttpStatus.OK);
    }

    @PostMapping("/addAgree")
    @ResponseBody
    public int agree(Long boardId, Authentication authentication) {
        Member findMember = findMember(authentication);

        return boardService.addAgree(findMember, boardId);
    }

    @GetMapping("/map")
    public String getMap() {
        return "/jinsang/map";
    }

    @ResponseBody
    @GetMapping("/listToMap")
    public ResponseEntity<List<BoardMapDto>> boardItemsToMap() {
        List<BoardMapDto> boardMapDto = boardService.getBoardInfoForMap();
        return ResponseEntity.ok().body(boardMapDto);
    }

    private Member findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).get();
    }
}
