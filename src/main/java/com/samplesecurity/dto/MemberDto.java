package com.samplesecurity.dto;

import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.MemberAuth;
import com.samplesecurity.domain.Profile;
import com.samplesecurity.dto.Board.AttachFileDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
    public class MemberDto {
        private Long id;
        private String email;
        private String password;
        private String nickName;
        private List<MemberAuth> roles;
        private Profile profile;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .nickName(nickName)
                .email(email)
                .password(password)
                .roles(roles)
                .build();
    }
}
