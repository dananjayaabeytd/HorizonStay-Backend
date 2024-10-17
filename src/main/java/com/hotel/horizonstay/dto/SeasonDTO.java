package com.hotel.horizonstay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeasonDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 214101981905645865L;

    private int statusCode;
    private String error;
    private String message;
    private Long id;
    private String seasonName;
    private LocalDate validFrom;
    private LocalDate validTo;
}
