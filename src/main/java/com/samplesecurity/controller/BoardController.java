package com.samplesecurity.controller;

import com.samplesecurity.domain.Board;
import com.samplesecurity.domain.Member;
import com.samplesecurity.dto.BoardRegisterDto;
import com.samplesecurity.repository.BoardRepository;
import com.samplesecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("board", boardRepository.findAll());

        return "board/list";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") Long id, Model model) {
        Board findBoard = boardRepository.findById(id).get();
        model.addAttribute("board", findBoard);

        return "board/get";
    }

    @GetMapping("/register")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String register() {
        return "board/register";
    }

    @PostMapping("/register")
    @Secured({"ROLE_MEMBER", "ROLE_ADMIN"})
    public String register(BoardRegisterDto boardRegisterDto, Authentication authentication) {
        log.info("boardDto: " + boardRegisterDto);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Member findMember = memberRepository.findByEmail(username).get();

        log.info("username: " + username);
        Board board = boardRegisterDto.toEntity();
        board.setMember(findMember);
        boardRepository.save(board);

        return "redirect:/board/list";
    }

    @PostMapping("/agree")
    @ResponseBody
    @Transactional
    public int agree(Long bno,int count, Authentication authentication) {
        System.out.println("bno = " + bno);
        System.out.println("count = " + count);
        if (authentication == null) {
            return -1;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Member findMember = memberRepository.findByEmail(username).get();

        Board board = boardRepository.findById(bno).get();
        if(!findMember.isAgreeCheck()){
            count++;
            findMember.setAgreeCheck(true);
            board.setLikeCount(count);
        }else{
            count--;
            findMember.setAgreeCheck(false);
            board.setLikeCount(count);
        }

        return count;
    }
}
