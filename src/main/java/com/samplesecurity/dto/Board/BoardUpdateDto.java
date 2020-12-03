package com.samplesecurity.dto.Board;

import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.board.AttachFile;
import com.samplesecurity.domain.board.Board;
import com.samplesecurity.domain.board.Category;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Float.parseFloat;

@Data
public class BoardUpdateDto {
    private String title;

    private String address;

    private String contents;

    private String category;

    private String latitude; //위도

    private String longitude; //경도

    private String boardType;

    private List<AttachFile> fileDtos;

}
