package com.samplesecurity.dto.Board;

import com.samplesecurity.domain.board.AttachFile;
import lombok.Data;

import java.util.List;

@Data
public class BoardUpdateDto {
    private String title;

    private String address;

    private String contents;

    private String category;

    private String latitude; //위도

    private String longitude; //경도

    private List<AttachFile> fileDtos;

}
