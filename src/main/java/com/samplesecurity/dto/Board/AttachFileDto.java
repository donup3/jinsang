package com.samplesecurity.dto.Board;


import com.samplesecurity.domain.board.AttachFile;
import lombok.Builder;
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

    @Builder
    public AttachFileDto(String fileName, String uploadPath, String uuid) {
        this.fileName = fileName;
        this.uploadPath = uploadPath;
        this.uuid = uuid;
    }
}
