package com.samplesecurity.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samplesecurity.domain.board.Board;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "board")
public class AttachFile {  //게시글 아래 첨부파일
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
