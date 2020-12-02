package com.samplesecurity.domain.board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UploadFile {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String saveFileName;

    private String filePath;

    private String contentType;

    private long size;

    private LocalDateTime registerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
