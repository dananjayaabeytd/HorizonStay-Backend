package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculationDTO {

    private int statusCode;
    private String error;
    private String message;

    private String checkIn;
    private String checkOut;
    private int noOfAdults;
    private int noOfChildren;
    private Float discountPercentage;
    private Float markupPercentage;

}
