package com.hotel.horizonstay.repository;


import com.hotel.horizonstay.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
