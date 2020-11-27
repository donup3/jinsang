package com.samplesecurity.dto.reply;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplyListDto {
    private Long replyId;

    private String contents;

    private int refidx;

    private int step;

    private int orders;

    private String memberName;

    private String toMemberName;

    private LocalDate createdDate;

    private int agreeCount;

    private int disagreeCount;

    @QueryProjection
    public ReplyListDto(Long replyId, String contents, int refidx, int step, int orders, String memberName, String toMemberName, LocalDate createdDate, int agreeCount, int disagreeCount) {
        this.replyId = replyId;
        this.contents = contents;
        this.refidx = refidx;
        this.step = step;
        this.orders = orders;
        this.memberName = memberName;
        this.toMemberName = toMemberName;
        this.createdDate = createdDate;
        this.agreeCount = agreeCount;
        this.disagreeCount = disagreeCount;
    }
}
