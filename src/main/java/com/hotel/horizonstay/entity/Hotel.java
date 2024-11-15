package com.hotel.horizonstay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Data
public class Hotel implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;

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

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> hotelImages;

    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelContract> contracts = new ArrayList<>();


}
