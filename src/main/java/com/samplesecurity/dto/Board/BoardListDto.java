package com.samplesecurity.dto.Board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BoardListDto {
    private Long id;

    private String category;

    private String title;

    private LocalDate createdDate;

    private String writer;

    private int agreeCount;

    private int replyCount;

    @QueryProjection
    public BoardListDto(Long id, String category, String title, LocalDate createdDate, String writer, int agreeCount, int replyCount) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.createdDate = createdDate;
        this.writer = writer;
        this.agreeCount = agreeCount;
        this.replyCount = replyCount;
    }
}
