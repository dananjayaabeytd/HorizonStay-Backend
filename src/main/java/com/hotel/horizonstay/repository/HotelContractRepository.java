package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.entity.HotelContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelContractRepository extends JpaRepository<HotelContract,Long> {

    HotelContract findByHotelAndValidFromAndValidTo(Hotel hotel, LocalDate validFrom, LocalDate validTo);

    @Query("SELECT hc FROM HotelContract hc " +
            "WHERE (hc.hotel.hotelCity = :location OR hc.hotel.hotelCountry = :location) AND " +
            "hc.validFrom <= :checkOutDate AND hc.validTo >= :checkInDate")
    List<HotelContract> findContractsByLocationAndDateRange(String location, LocalDate checkInDate, LocalDate checkOutDate);

    List<HotelContract> findByHotel_HotelID(Long hotelId);
}
