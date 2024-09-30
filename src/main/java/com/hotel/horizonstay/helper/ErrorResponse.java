package com.hotel.horizonstay.helper;

import com.hotel.horizonstay.dto.HotelContractDTO;
import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.dto.SearchResultDTO;
import com.hotel.horizonstay.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ErrorResponse {
    // Helper method to create an error response
    public ResponseEntity<HotelDTO> createHotelErrorResponse(String message, HttpStatus status)
    {
        HotelDTO errorResponse = new HotelDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    // Helper method to create an error response
    public ResponseEntity<UserDTO> createErrorResponse(String message, HttpStatus status)
    {
        UserDTO errorResponse = new UserDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    public ResponseEntity<HotelContractDTO> createContractErrorResponse(String message, HttpStatus httpStatus) {
        return null;
    }

    public ResponseEntity<List<SearchResultDTO>> createSearchErrorResponseList(String message, HttpStatus httpStatus) {
        return null;
    }

    public ResponseEntity<List<HotelContractDTO>> createErrorResponseList(String errorOccurredWhileFetchingContracts, HttpStatus httpStatus) {
    return null;
    }
}
