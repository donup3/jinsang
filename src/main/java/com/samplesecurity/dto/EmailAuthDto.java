package com.samplesecurity.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class EmailAuthDto {
    private Long id;
    private String email;
    private String authCode;
    private Long memberId;

    @Builder
    public EmailAuthDto(Long id, String email, String authCode) {
        this.id = id;
        this.email = email;
        this.authCode = authCode;
    }
}
