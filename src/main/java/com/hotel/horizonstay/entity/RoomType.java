package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "roomtype")
@Data
public class RoomType implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomTypeName;
    private Integer numberOfRooms;
    private Integer maxNumberOfPersons;
    private Float price;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> roomTypeImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

}


