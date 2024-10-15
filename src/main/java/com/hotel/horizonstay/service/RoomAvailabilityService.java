package com.hotel.horizonstay.service;

import com.hotel.horizonstay.repository.RoomAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RoomAvailabilityService {

    @Autowired
    RoomAvailabilityRepository roomAvailabilityRepository;

    public void deleteExpiredRoomAvailabilities() {
        LocalDate today = LocalDate.now();
        roomAvailabilityRepository.deleteByCheckOutDate(today);
    }
}