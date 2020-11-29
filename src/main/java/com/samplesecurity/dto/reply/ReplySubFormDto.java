package com.samplesecurity.dto.reply;

import com.samplesecurity.domain.Reply;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplySubFormDto {

    private Long replyId;

    private String contents;

    private int ref;

    private int level;

    private int refOrder;

    public Reply toEntity(){
        return Reply.builder()
                .contents(contents)
                .createdDate(LocalDate.now())
                .agreeCount(0)
                .disagreeCount(0)
                .level(level)
                .refOrder(refOrder)
                .ref(ref)
                .build();
    }
}
