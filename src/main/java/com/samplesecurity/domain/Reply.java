package com.samplesecurity.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = {"board","member"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "js_reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String contents;

    private int ref; //댓글 그룹번호

    private int level; // depth

    private int refOrder; // 같은 댓글 그룹안에서 순서

    private LocalDate createdDate;

    private int agreeCount;

    private int disagreeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    //member 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;    //보내는 사람

    private String toMemberName;  //받는 사람
}
