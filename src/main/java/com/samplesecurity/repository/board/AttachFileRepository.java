package com.samplesecurity.repository.board;

import com.samplesecurity.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachFileRepository extends JpaRepository<AttachFile, String> {
    List<AttachFile> findByBoardId(Long boardId);

}
