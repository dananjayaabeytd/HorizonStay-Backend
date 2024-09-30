package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "roomtype")
@Data
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomTypeName;
    private Integer numberOfRooms;
    private Integer maxNumberOfPersons;
    private Float price;

    @ElementCollection
    private List<String> roomTypeImages;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

}


