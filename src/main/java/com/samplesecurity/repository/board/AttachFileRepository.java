package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachFileRepository extends JpaRepository<AttachFile, String> {
    List<AttachFile> findByBoardId(Long boardId);

    List<AttachFile> findAllByBoardId(Long boardId);

    void deleteByBoardId(Long boardId);
}
