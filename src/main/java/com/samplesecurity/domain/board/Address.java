package com.samplesecurity.domain.board;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "js_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String cityName;

    @OneToMany(mappedBy = "cityNameOfAddress")
    private List<Board> boards=new ArrayList<>();
}
