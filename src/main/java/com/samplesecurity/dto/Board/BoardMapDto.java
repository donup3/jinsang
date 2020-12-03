package com.samplesecurity.dto.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BoardMapDto {
    private Long id;
    private String title;
    private Float latitude;
    private Float longitude;

    @Builder
    public BoardMapDto(Long id, String title, Float latitude, Float longitude) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
