package com.samplesecurity.domain;

import com.samplesecurity.domain.board.Board;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    private String nickName;

    @OneToMany(mappedBy = "member")
    private List<MemberAuth> roles;

    @OneToMany(mappedBy = "member")
    private List<Board> boards;

    @OneToOne(mappedBy = "member")
    private Profile profile;

    @OneToOne(mappedBy = "member")
    private EmailAuth emailAuth;
}