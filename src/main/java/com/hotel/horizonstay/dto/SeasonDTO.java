package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeasonDTO {

    private int statusCode;
    private String error;
    private String message;

    private Long id;
    private String seasonName;
    private LocalDate validFrom;
    private LocalDate validTo;
//    private List<RoomTypeDTO> roomTypes;
//    private List<SupplementDTO> supplements;
//    private List<DiscountDTO> discounts;
//    private List<MarkupDTO> markups;
}
