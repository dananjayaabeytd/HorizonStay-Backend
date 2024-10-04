package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.HotelContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<HotelContract, Long> {
    List<HotelContract> findByHotel_HotelID(Long hotelId);
}
