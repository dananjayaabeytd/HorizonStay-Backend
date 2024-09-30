package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Booking;
import com.hotel.horizonstay.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByEmail(String email);

    List<Booking> findBySystemUser(SystemUser systemUser);

}
