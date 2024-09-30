package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    @Query("SELECT s FROM Season s WHERE s.contract = :contract AND " +
            "(s.validFrom <= :checkOutDate AND s.validTo >= :checkInDate)")
    List<Season> findActiveSeasonsByContract(HotelContract contract, LocalDate checkInDate, LocalDate checkOutDate);

}
