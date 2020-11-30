package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
