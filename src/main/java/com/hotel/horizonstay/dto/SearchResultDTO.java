package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultDTO {

    private int statusCode;
    private String error;
    private String message;

    private Long number;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String cancellationPolicy;
    private String paymentPolicy;
    private List<SeasonDTO> seasons;

    private Long hotelID;
    private String hotelName;
    private String hotelLocation;
    private String hotelDescription;
    private String hotelContactNumber;
    private String hotelEmail;
    private Float hotelRating;
    private List<String> hotelImages;

}