package com.hotel.horizonstay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "bookingitems")
@Data
public class BookingItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String name;
    private float price;
    private int quantity;
    private float totalAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;

}
