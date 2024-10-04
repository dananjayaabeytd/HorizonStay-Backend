package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomTypeDTO {

    private int statusCode;
    private String error;
    private String message;

    private Long roomTypeID;
    private String roomTypeName;
    private Integer numberOfRooms;
    private Integer maxNumberOfPersons;
    private Float price;
    private List<String> roomTypeImages;
}
