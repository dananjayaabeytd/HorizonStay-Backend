package com.hotel.horizonstay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Data
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelID;

    private String hotelName;
    private String hotelDescription;
    private String hotelContactNumber;
    private String hotelCity;
    private String hotelCountry;
    private String hotelEmail;
    private Float hotelRating;

    @ElementCollection
    private List<String> hotelImages;

    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelContract> contracts = new ArrayList<>();


}
