package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.AgreeCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreeCheckRepository extends JpaRepository<AgreeCheck, Long> {
    List<AgreeCheck> findAllByBoardId(Long boardId);
    AgreeCheck findByMemberIdAndBoardId(Long memberId, Long boardId);

}
