package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void 주소구분생성(){
        Address address1=Address.builder().cityName("서울").build();
        addressRepository.save(address1);
        Address address2=Address.builder().cityName("인천").build();
        addressRepository.save(address2);
        Address address3=Address.builder().cityName("경기").build();
        addressRepository.save(address3);
        Address address4=Address.builder().cityName("강원").build();
        addressRepository.save(address4);
        Address address5=Address.builder().cityName("충북").build();
        addressRepository.save(address5);
        Address address6=Address.builder().cityName("충남").build();
        addressRepository.save(address6);
        Address address7=Address.builder().cityName("대전").build();
        addressRepository.save(address7);
        Address address8=Address.builder().cityName("경북").build();
        addressRepository.save(address8);
        Address address9=Address.builder().cityName("울산").build();
        addressRepository.save(address9);
        Address address10=Address.builder().cityName("대구").build();
        addressRepository.save(address10);
        Address address11=Address.builder().cityName("경남").build();
        addressRepository.save(address11);
        Address address12=Address.builder().cityName("부산").build();
        addressRepository.save(address12);
        Address address13=Address.builder().cityName("전북").build();
        addressRepository.save(address13);
        Address address14=Address.builder().cityName("전남").build();
        addressRepository.save(address14);
        Address address15=Address.builder().cityName("광주").build();
        addressRepository.save(address15);
        Address address16=Address.builder().cityName("제주").build();
        addressRepository.save(address16);
    }
}