package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {

    List<Season> findByContractId(Long contractID);
    List<Season> findSeasonsByContract(HotelContract contract);
}
