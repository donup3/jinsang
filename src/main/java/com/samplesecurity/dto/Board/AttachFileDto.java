package com.samplesecurity.dto.Board;

import com.samplesecurity.domain.board.AttachFile;
import lombok.Data;

@Data
public class AttachFileDto {

    private String fileName;

    private String uploadPath;

    private String uuid;

    public AttachFile toEntity(){
        return AttachFile.builder()
                .fileName(fileName)
                .uploadPath(uploadPath)
                .uuid(uuid)
                .build();
    }
}
