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
        return "jinsang/write";
    }

    @PostMapping("/write")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String register(BoardRegisterDto boardRegisterDto,
                           Authentication authentication,
                           RedirectAttributes rttr) {
        log.info("boardDto: " + boardRegisterDto);
        Member findMember = findMember(authentication);
        Category category = categoryRepository.findById(parseLong(boardRegisterDto.getCategory())).get();
        Board board = boardRegisterDto.toEntity(findMember, category);
        List<AttachFileDto> fileDtos = boardRegisterDto.getFileDtos();

        boardService.register(board, fileDtos);

        rttr.addAttribute("boardType", boardRegisterDto.getBoardType());

        return "redirect:/jinsang/jslist";
    }

    @GetMapping("/modify/{boardId}")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String modifyForm(@PathVariable("boardId") Long boardId,
                             Authentication authentication,
                             @ModelAttribute("pageDto") BoardPageDto boardPageDto,
                             Model model) {
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
                         RedirectAttributes rttr) {
        log.info("modify boardUpdateDto: " + boardUpdateDto);
        log.info("boardType: " + boardUpdateDto.getBoardType());
        Member findMember = findMember(authentication);

        boardService.update(findMember, boardId, boardUpdateDto);

        rttr.addAttribute("boardType", boardUpdateDto.getBoardType());

        return "redirect:/jinsang/jslist";
    }

    @PostMapping("/delete/{boardId}")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String delete(@PathVariable("boardId") Long boardId,
                         BoardPageDto boardPageDto,
                         Authentication authentication,
                         RedirectAttributes rttr) {
        Member findMember = findMember(authentication);
        boardService.delete(boardId, findMember);

        rttr.addAttribute("page", boardPageDto.getPage());
        rttr.addAttribute("boardType", boardPageDto.getBoardType());

        return "redirect:/jinsang/jslist";
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

    private Member findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).get();
    }
}
