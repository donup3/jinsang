package com.samplesecurity.dto.Board;

import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.board.Category;
import com.samplesecurity.domain.Member;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;

@Data
public class BoardRegisterDto {
    private String title;
    private String address;
    private String contents;
    private String category;
    private String latitude; //위도
    private String boardType;
    private String longitude; //경도
    private String hidden;
    private List<AttachFileDto> fileDtos = new ArrayList<>();

    public Board toEntity(Member member, Category category) {
        Board board = Board.builder()
                .title(title)
                .address(address)
                .contents(contents)
                .latitude(parseFloat(latitude))
                .longitude(parseFloat(longitude))
                .boardType(boardType)
                .category(category)
                .agreeCount(0)
                .hidden(hidden)
                .createdDate(LocalDate.now())
                .build();
        board.setMember(member);

        return board;
    }

    public Board toEntityOfKnow(Member member, Category category) {
        Board board = Board.builder()
                .title(title)
                .contents(contents)
                .boardType(boardType)
                .category(category)
                .agreeCount(0)
                .createdDate(LocalDate.now())
                .hidden(hidden)
                .build();
        board.setMember(member);

        return board;
    }
}
