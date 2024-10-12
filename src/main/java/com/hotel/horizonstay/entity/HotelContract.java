package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotelcontracts")
@Data
public class HotelContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate validFrom;
    private LocalDate validTo;
    private String cancellationPolicy;
    private String paymentPolicy;
    private LocalDateTime addedDate;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<Season> seasons= new ArrayList<>();

}


