package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotelcontracts")
@Data
public class HotelContract implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String cancellationPolicy;
    private String paymentPolicy;
    private LocalDateTime addedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Season> seasons= new ArrayList<>();

}


