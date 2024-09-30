package com.hotel.horizonstay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private String fullName;
    private String telephone;
    private String email;
    private String address;
    private String city;
    private String country;
    private Long hotelID;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private int noOfAdults;
    private int noOfChildren;

    private float discount;
    private float payableAmount;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingItem> items;

    @ManyToOne(optional = true)
    @JoinColumn(name = "system_user_id")
    private SystemUser systemUser;

}
