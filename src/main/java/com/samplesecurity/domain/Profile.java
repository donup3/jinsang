package com.samplesecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @Column(name = "profile_uuid", nullable = false)
    private String uuid;

    @Column(name = "uploadPath", nullable = false)
    private String uploadPath;

    @Column(name = "fileName", nullable = false)
    private String fileName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
