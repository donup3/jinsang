package com.samplesecurity.dto;

import com.samplesecurity.domain.Profile;
import lombok.Builder;
import lombok.Data;

@Data
public class ProfileDto {
    private String uuid;
    private String uploadPath;
    private String fileName;

    @Builder
    public ProfileDto(String uuid, String uploadPath, String fileName) {
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.fileName = fileName;
    }
}
