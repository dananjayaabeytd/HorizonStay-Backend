package com.hotel.horizonstay.helper;

import com.hotel.horizonstay.service.RoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoomAvailabilityCleanupTask {

    @Autowired
    RoomAvailabilityService roomAvailabilityService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void cleanUpExpiredRoomAvailabilities() {
        roomAvailabilityService.deleteExpiredRoomAvailabilities();
    }
}