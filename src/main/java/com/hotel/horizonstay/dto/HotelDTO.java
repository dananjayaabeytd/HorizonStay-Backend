package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotel.horizonstay.entity.Hotel;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelDTO {

    private int statusCode;
    private String error;
    private String message;

    private Long hotelID;
    private String hotelName;
    private String hotelEmail;
    private String hotelDescription;
    private String hotelContactNumber;
    private String hotelCity;
    private String hotelCountry;
    private Float hotelRating;
    private List<String> hotelImages;

    private List<Hotel> hotelList;

}
