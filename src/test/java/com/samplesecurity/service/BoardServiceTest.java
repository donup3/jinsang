package com.samplesecurity.service;

import com.samplesecurity.dto.Board.BoardMapDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest
@Rollback
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void 게시판데이터_To_Map(){
        List<BoardMapDto> boardMapDtos = boardService.getBoardInfoForMap();

        boardMapDtos.forEach(item -> System.out.println("item : " + item));
    }
}