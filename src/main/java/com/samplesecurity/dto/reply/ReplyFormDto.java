package com.samplesecurity.dto.reply;

import com.samplesecurity.domain.reply.Reply;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplyFormDto {

    private String contents;

    //board,member 세팅은 service에서 함
    //Ref 값은 service에서 +1
    public Reply toEntity(){
        return Reply.builder()
                .contents(contents)
                .createdDate(LocalDate.now())
                .agreeCount(0)
                .disagreeCount(0)
                .level(0)
                .refOrder(0)
                .build();
    }

}
