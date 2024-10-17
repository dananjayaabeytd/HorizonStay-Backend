package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomTypeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L; // Version control for serialization

    private int statusCode;
    private String error;
    private String message;
    private Long roomTypeID;
    private String roomTypeName;
    private Integer numberOfRooms;
    private Integer maxNumberOfPersons;
    private Float price;
    private List<String> roomTypeImages;
    private Integer availableRooms;
}
