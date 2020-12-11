package com.samplesecurity.controller;

import com.samplesecurity.domain.board.Board;
import com.samplesecurity.dto.Board.HomeBoardDto;
import com.samplesecurity.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Slf4j

@RequiredArgsConstructor
public class HomeController {

    private final BoardRepository boardRepository;

    @GetMapping("/")
    public String index(HttpServletResponse response, Model model) {
        createCookie(response);

        List<Board> boardsOfType1 = boardRepository.findAllByType1();
        model.addAttribute("board1", boardsOfType1);

        List<Board> boardsOfType2 = boardRepository.findAllByType2();
        model.addAttribute("board2", boardsOfType2);

        List<Board> boardsOfType3 = boardRepository.findAllByType3();
        model.addAttribute("board3", boardsOfType3);

        List<Board> boardsOfType4 =boardRepository.findAllByType4();
        model.addAttribute("board4", boardsOfType4);

        List<HomeBoardDto> address = boardRepository.findCountByAddress();
        model.addAttribute("address",address);

        return "/index";
    }

    private void createCookie(HttpServletResponse response) {
        Cookie cookie =new Cookie("view",null);
        cookie.setComment("게시글 조회 확인");
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
    }
}
