package com.samplesecurity.repository.board;

import com.samplesecurity.domain.board.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByCityName(String cityName);
}
