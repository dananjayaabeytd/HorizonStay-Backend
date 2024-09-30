package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "season")
@Data
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seasonName;
    private LocalDate validFrom;
    private LocalDate validTo;

    @ManyToOne
    @JoinColumn(name = "hotel_contract_id")
    private HotelContract contract;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<RoomType> roomTypes;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Supplement> supplements;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Discount> discounts;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Markup> markups;

}

