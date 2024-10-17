package com.hotel.horizonstay.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class RoomAvailability implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numberOfRooms; // Changed from selectedRooms to numberOfRooms
    private LocalDate checkIn;
    private LocalDate checkOut;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false) // Foreign key to RoomType
    private RoomType roomType;

}