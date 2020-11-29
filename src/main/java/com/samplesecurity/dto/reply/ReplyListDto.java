package com.samplesecurity.dto.reply;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplyListDto {
    private Long replyId;

    private String contents;

    private int ref;

    private int level;

    private int refOrder;

    private String memberName;

    private String toMemberName;

    private LocalDate createdDate;

    private int agreeCount;

    private int disagreeCount;

    @QueryProjection
    public ReplyListDto(Long replyId, String contents, int ref, int level, int orders, String memberName, String toMemberName, LocalDate createdDate, int agreeCount, int disagreeCount) {
        this.replyId = replyId;
        this.contents = contents;
        this.ref = ref;
        this.level = level;
        this.refOrder = orders;
        this.memberName = memberName;
        this.toMemberName = toMemberName;
        this.createdDate = createdDate;
        this.agreeCount = agreeCount;
        this.disagreeCount = disagreeCount;
    }
}
