package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDTO {

    private Long bookingId;
    private int statusCode;
    private String error;
    private String message;

    private String fullName;
    private String telephone;
    private String email;
    private String address;
    private String city;
    private String country;

    private String checkIn;
    private String checkOut;

    private int noOfAdults;
    private int noOfChildren;

    private float discount;
    private float payableAmount;
    private Long hotelID;

    private String hotelName;
    private String hotelLocation;
    private String hotelDescription;
    private String hotelContactNumber;
    private String hotelEmail;
    private Float hotelRating;
    private List<String> hotelImages;

    private List<BookingItemDTO> items;
    private List<BookingDTO> bookingList;

    private Integer systemUserId;


}
