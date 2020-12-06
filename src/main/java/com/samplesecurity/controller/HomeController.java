package com.samplesecurity.controller;

import com.samplesecurity.domain.board.Board;
import com.samplesecurity.dto.Board.HomeBoardDto;
import com.samplesecurity.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j

@RequiredArgsConstructor
public class HomeController {

    private final BoardRepository boardRepository;

    @GetMapping("/")
    public String index(Model model) {
        log.info("main@@@@@@");
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
}
