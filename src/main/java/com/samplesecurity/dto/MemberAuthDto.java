package com.samplesecurity.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberAuthDto {
    private Long id;
    private String role;

    @Builder
    public MemberAuthDto(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
