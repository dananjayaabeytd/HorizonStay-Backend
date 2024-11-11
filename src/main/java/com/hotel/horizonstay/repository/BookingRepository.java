package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Booking;
import com.hotel.horizonstay.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>
{
    List<Booking> findByEmail(String email);

    List<Booking> findBySystemUser(SystemUser systemUser);

}
