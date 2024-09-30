package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
