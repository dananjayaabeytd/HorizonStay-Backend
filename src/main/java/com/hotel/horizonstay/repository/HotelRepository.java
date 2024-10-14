package com.hotel.horizonstay.repository;


import com.hotel.horizonstay.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h WHERE CONCAT(h.hotelCity, ', ', h.hotelCountry) = :location")
    List<Hotel> findByLocation(@Param("location") String location);

    Optional<Hotel> findByHotelName(String hotelName);

    Optional<Hotel> findByHotelEmail(String hotelEmail);
}
