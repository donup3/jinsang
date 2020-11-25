package com.samplesecurity.repository;

import com.samplesecurity.domain.AgreeCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreeCheckRepository extends JpaRepository<AgreeCheck, Long> {
    List<AgreeCheck> findAllByBoardId(Long boardId);
    AgreeCheck findByMemberIdAndBoardId(Long memberId, Long boardId);
}
