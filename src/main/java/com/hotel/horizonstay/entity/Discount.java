package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "discounts")
@Data
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String discountName;
    private Float percentage;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

}

