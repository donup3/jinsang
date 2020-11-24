package com.samplesecurity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "board")
public class AttachFile {
    @Id
    @Column(name = "board_attachFile_uuid", nullable = false)
    private String uuid;

    @Column(name = "uploadPath", nullable = false)
    private String uploadPath;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }
}
