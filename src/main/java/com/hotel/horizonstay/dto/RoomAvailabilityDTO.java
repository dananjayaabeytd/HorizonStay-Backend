package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomAvailabilityDTO {

    private LocalDate checkIn;
    private LocalDate checkOut;
    private List<RoomTypeDTO> roomTypes;
    private Integer statusCode;
    private String message;

    @Data
    public static class RoomTypeDTO {
        private Long roomTypeID;
        private Integer numberOfRooms;
    }

}