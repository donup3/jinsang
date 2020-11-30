package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> ,BoardCustomRepository{

}
