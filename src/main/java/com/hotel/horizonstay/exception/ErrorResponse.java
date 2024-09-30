package com.hotel.horizonstay.exception;

import com.hotel.horizonstay.dto.HotelDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    // Helper method to create an error response
    public ResponseEntity<HotelDTO> createHotelErrorResponse(String message, HttpStatus status)
    {
        HotelDTO errorResponse = new HotelDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
