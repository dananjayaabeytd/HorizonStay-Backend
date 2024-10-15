package com.hotel.horizonstay.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class RoomAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfRooms; // Changed from selectedRooms to numberOfRooms

    private LocalDate checkIn;

    private LocalDate checkOut;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false) // Foreign key to RoomType
    private RoomType roomType;

}