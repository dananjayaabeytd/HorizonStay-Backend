package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "markup")
@Data
public class Markup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String markupName;
    private Float percentage;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

}


