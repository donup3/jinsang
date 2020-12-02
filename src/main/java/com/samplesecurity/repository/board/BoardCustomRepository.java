package com.samplesecurity.repository.board;

import com.samplesecurity.dto.Board.BoardListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    Page<BoardListDto> findAllByDto(String boardType, Pageable pageable);

    Long findPreBoardId(Long boardId, String boardType);

    Long findNextBoardId(Long boardId, String boardType);
}
