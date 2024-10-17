package com.hotel.horizonstay.repository;
import com.hotel.horizonstay.entity.HotelContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<HotelContract, Long> {

    List<HotelContract> findByHotel_HotelID(Long hotelId);

    @Query("SELECT hc FROM HotelContract hc " +
            "WHERE (hc.hotel.hotelCity = :location OR hc.hotel.hotelCountry = :location) AND " +
            "hc.validFrom <= :checkOutDate AND hc.validTo >= :checkInDate")
    List<HotelContract> findContractsByLocationAndDateRange(String location, LocalDate checkInDate, LocalDate checkOutDate);

//    @Query("SELECT hc FROM HotelContract hc " +
//            "WHERE (hc.hotel.hotelCity = :location OR hc.hotel.hotelCountry = :location) AND " +
//            "hc.validFrom <= :checkOutDate AND hc.validTo >= :checkInDate")
//    Page<HotelContract> findContractsByLocationAndDateRange(String location, LocalDate checkInDate, LocalDate checkOutDate, Pageable pageable);

}
