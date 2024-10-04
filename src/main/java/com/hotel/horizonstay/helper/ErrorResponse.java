package com.hotel.horizonstay.helper;

import com.hotel.horizonstay.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    //Helper method to create an error response
    public ResponseEntity<HotelContractDTO> createContractErrorResponse(String message, HttpStatus status)
    {
        HotelContractDTO errorResponse = new HotelContractDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    //Helper method to create an error response for List
    public ResponseEntity<List<SearchResultDTO>> createSearchErrorResponseList(String message, HttpStatus status)
    {
        SearchResultDTO errorResponse = new SearchResultDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);

        return ResponseEntity.status(status).body(Collections.singletonList(errorResponse));
    }

    //Helper method to create an error response for List
    public ResponseEntity<List<HotelContractDTO>> createErrorResponseList(String message, HttpStatus status)
    {
        HotelContractDTO errorResponse = new HotelContractDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);

        return ResponseEntity.status(status).body(Collections.singletonList(errorResponse));
    }

    // Helper method to create an error response for Map
    public ResponseEntity<Map<String, Object>> createErrorResponseMap(String message, HttpStatus status)
    {
        return ResponseEntity.status(status).body(Collections.singletonMap("error", message));
    }

    public ResponseEntity<BookingDTO> createErrorResponseBooking(String message, HttpStatus status) {
        BookingDTO errorResponse = new BookingDTO();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    public ResponseEntity<SeasonDTO> createSeasonErrorResponse(String message, HttpStatus httpStatus) {
        SeasonDTO errorResponse = new SeasonDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<List<SeasonDTO>> createSeasonErrorResponseList(String errorOccurredWhileFetchingSeasons, HttpStatus httpStatus) {
        SeasonDTO errorResponse = new SeasonDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(errorOccurredWhileFetchingSeasons);
        return ResponseEntity.status(httpStatus).body(Collections.singletonList(errorResponse));
    }

    public ResponseEntity<RoomTypeDTO> createRoomTypeErrorResponse(String message, HttpStatus httpStatus) {
        RoomTypeDTO errorResponse = new RoomTypeDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<MarkupDTO> createMarkupErrorResponse(String errorOccurredWhileFetchingMarkup, HttpStatus httpStatus) {
        MarkupDTO errorResponse = new MarkupDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(errorOccurredWhileFetchingMarkup);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<DiscountDTO> createDiscountErrorResponse(String message, HttpStatus httpStatus) {
        DiscountDTO errorResponse = new DiscountDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(message);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<SupplementDTO> createSupplementErrorResponse(String errorOccurredWhileFetchingSupplement, HttpStatus httpStatus) {
        SupplementDTO errorResponse = new SupplementDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(errorOccurredWhileFetchingSupplement);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    public ResponseEntity<List<RoomTypeDTO>> createRoomtErrorResponseList(String s, HttpStatus httpStatus) {
        RoomTypeDTO errorResponse = new RoomTypeDTO();
        errorResponse.setStatusCode(httpStatus.value());
        errorResponse.setMessage(s);
        return ResponseEntity.status(httpStatus).body(Collections.singletonList(errorResponse));
    }
}
