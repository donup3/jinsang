package com.samplesecurity.repository.board;

import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.board.Address;
import com.samplesecurity.domain.board.Board;
import com.samplesecurity.dto.Board.BoardListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardCustomRepository {
    Page<BoardListDto> findAllByDto(String boardType, Pageable pageable);

    Long findPreBoardId(Long boardId, String boardType);

    Long findNextBoardId(Long boardId, String boardType);

    List<Board> findAllByType1();

    List<Board> findAllByType2();

    List<Board> findAllByType3();

    List<Board> findAllByType4();

    List<Address> findCountByAddress();
}
