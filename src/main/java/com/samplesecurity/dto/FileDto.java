package com.samplesecurity.dto;

import lombok.Data;

@Data
public class FileDto {
    private String fileName;

    private String uploadPath;

    private String uuid;

    private boolean image;
}
