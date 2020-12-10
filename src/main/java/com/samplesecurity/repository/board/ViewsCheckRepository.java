package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.ViewsCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewsCheckRepository extends JpaRepository<ViewsCheck, Long> {
    ViewsCheck findByMemberIdAndBoardId(Long id, Long boardId);
}
