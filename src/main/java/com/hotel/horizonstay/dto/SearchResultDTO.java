package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotel.horizonstay.entity.Discount;
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
    private Long hotelID;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String cancellationPolicy;
    private String paymentPolicy;

    private String hotelName;
    private String hotelLocation;
    private String hotelDescription;
    private String hotelContactNumber;
    private String hotelEmail;
    private Float hotelRating;
    private List<String> hotelImages;

    private SeasonDTO seasonDTO;
    private MarkupDTO markupDTO;
    private List<RoomTypeDTO> roomTypeDTO;
    private List<DiscountDTO> discountDTO;
    private List<SupplementDTO> supplementDTOS;

}
