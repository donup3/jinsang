package com.samplesecurity.repository;

import com.samplesecurity.domain.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfileRepositoryTests {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void UUID를_이용한_데이터_검색() {
        String uuid = "f514e002-cf9f-431b-9879-d81c280a445e";

        Profile profile = profileRepository.findByUuid(uuid).get();

        System.out.println("------------------------------------");
        System.out.println(profile);
        System.out.println("------------------------------------");

        assertEquals(profile.getMember().getId(), 1);
    }
}