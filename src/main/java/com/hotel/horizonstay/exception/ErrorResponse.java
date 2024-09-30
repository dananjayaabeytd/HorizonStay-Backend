package com.hotel.horizonstay.exception;

import com.hotel.horizonstay.dto.HotelDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    public ResponseEntity<HotelDTO> createHotelErrorResponse(String hotelNotFound, HttpStatus httpStatus) {
        return null;
    }
}
