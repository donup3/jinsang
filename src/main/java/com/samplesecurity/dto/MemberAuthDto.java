package com.samplesecurity.dto;

import com.samplesecurity.domain.MemberAuth;
import com.samplesecurity.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberAuthDto {
    private Long id;
    private String role;

    public MemberAuth toEntity() {
        return MemberAuth.builder()
                .role(role)
                .build();
    }

    @Builder
    public MemberAuthDto(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
