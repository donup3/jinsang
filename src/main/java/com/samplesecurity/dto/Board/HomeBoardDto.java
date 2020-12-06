package com.samplesecurity.dto.Board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HomeBoardDto {
    private String city;

    private int count;

    @QueryProjection
    public HomeBoardDto(String city, int count) {
        this.city = city;
        this.count = count;
    }
}
