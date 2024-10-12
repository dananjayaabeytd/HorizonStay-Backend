package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelContractDTO {

    private int statusCode;
    private String error;
    private String message;

    private Long id;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String cancellationPolicy;
    private String paymentPolicy;
    private List<SeasonDTO> seasons;
    private LocalDateTime addedDate;

    private String hotelName;
    private String hotelLocation;

}
