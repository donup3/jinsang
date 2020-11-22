package com.samplesecurity.dto;

import com.samplesecurity.domain.Board;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BoardRegisterDto {

    private String title;

    private String address;

    private String content;

    private String writer;

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .address(address)
                .content(content)
                .writer(writer)
                .likeCount(0)
                .createdDate(LocalDate.now())
                .build();
    }
}
