package com.samplesecurity.dto.Board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BoardListDto {
    private Long id;

    private Long idx;

    private String category;

    private String title;

    private LocalDate createdDate;

    private String writer;

    private int agreeCount;

    private int replyCount;

    private String hidden;

    private int view;

    @QueryProjection
    public BoardListDto(Long id, Long idx, String category, String title, LocalDate createdDate, String writer, int agreeCount, int replyCount, String hidden, int view) {
        this.id = id;
        this.idx = idx;
        this.category = category;
        this.title = title;
        this.createdDate = createdDate;
        this.writer = writer;
        this.agreeCount = agreeCount;
        this.replyCount = replyCount;
        this.hidden = hidden;
        this.view = view;
    }
}
