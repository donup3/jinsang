package com.samplesecurity.domain.board;

import com.samplesecurity.domain.Member;
import com.samplesecurity.domain.reply.Reply;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "js_board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String contents;

    private int agreeCount;

    private Float latitude; //위도

    private Float longitude; //경도

    private String address;

    private LocalDate createdDate;

    private String boardType; //게시물 구분분

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; //게시물 분류 ex) 연인, 음식점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //attach 파일들은 단방향 매핑해놓음
    @OneToMany(mappedBy = "board")
    private List<UploadFile> uploadFiles;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies;

    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
