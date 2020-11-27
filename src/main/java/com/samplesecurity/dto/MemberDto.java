package com.samplesecurity.dto;

import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .nickName(nickName)
                .email(email)
                .password(password)
                .role(Role.MEMBER)
                .build();
    }
}
