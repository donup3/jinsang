package com.samplesecurity.dto.reply;

import com.samplesecurity.domain.Reply;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplyFormDto {

//    private String memberName; controller에서 로그인한 유저 받음

    private String contents;

    private String toMemberName;

    //board,member 세팅은 service에서 함
    public Reply toEntity(){
        return Reply.builder()
                .toMemberName(toMemberName)
                .contents(contents)
                .createdDate(LocalDate.now())
                .agreeCount(0)
                .disagreeCount(0)
                .orders(0)
                .refidx(0)
                .step(0)
                .build();
    }

}
