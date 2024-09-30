package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "supplement")
@Data
public class Supplement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplementName;
    private Float price;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

}


