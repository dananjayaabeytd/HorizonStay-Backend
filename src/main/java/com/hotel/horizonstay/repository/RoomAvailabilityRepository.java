package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Markup;
import com.hotel.horizonstay.entity.RoomAvailability;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

    @Query("SELECT ra FROM RoomAvailability ra WHERE ra.roomType.id = :roomTypeId AND ra.checkIn <= :checkOutDate AND ra.checkOut >= :checkInDate")
    List<RoomAvailability> findRoomAvailabilitiesByRoomTypeAndDateRange(@Param("roomTypeId") Long roomTypeId, @Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM RoomAvailability ra WHERE ra.checkOut <= :checkOutDate")
    void deleteByCheckOutDate(@Param("checkOutDate") LocalDate checkOutDate);

}
