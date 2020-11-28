package com.samplesecurity.dto;

import com.samplesecurity.domain.Profile;
import lombok.Data;

@Data
public class ProfileDto {
    private String uuid;
    private String uploadPath;
    private String fileName;

    public Profile toEntity() {
        return Profile.builder()
                .uuid(uuid)
                .uploadPath(uploadPath)
                .fileName(fileName)
                .build();
    }
}
