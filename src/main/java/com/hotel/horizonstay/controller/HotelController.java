package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.exception.ErrorResponse;
import com.hotel.horizonstay.exception.Validation;
import com.hotel.horizonstay.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HotelController {

    @Autowired
    private HotelService hotelService;

    private final Validation validation = new Validation();
    private final ErrorResponse error = new ErrorResponse();

    // Endpoint to get all hotels
    @GetMapping("admin/hotel/get-all")
    public ResponseEntity<HotelDTO> getAllHotels()
    {
        try
        {
            HotelDTO hotels = hotelService.getAllHotels();
            return ResponseEntity.ok(hotels);
        }
        catch (Exception e)
        {
            // Create error response in case of exception
            HotelDTO errorResponse = new HotelDTO();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("Error occurred while fetching hotels");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint to get a hotel by ID
    @GetMapping("admin/hotel/get/{hotelID}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long hotelID)
    {
        try
        {
            HotelDTO hotel = hotelService.getHotelById(hotelID);

            if (hotel != null)
            {
                return ResponseEntity.ok(hotel);
            }
            return error.createHotelErrorResponse("Hotel not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while fetching hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to add a new hotel
    @PostMapping("admin/hotel/add")
    public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO)
    {
        if (hotelDTO == null)
        {
            return error.createHotelErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }
        if (validation.isInvalidHotelData(hotelDTO))
        {
            return error.createHotelErrorResponse("Invalid hotel data", HttpStatus.BAD_REQUEST);
        }
        try
        {
            HotelDTO addedHotel = hotelService.addHotel(hotelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedHotel);
        }
        catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while adding hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to update an existing hotel
    @PutMapping("admin/hotel/update/{hotelID}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable Long hotelID, @RequestBody HotelDTO hotelDTO)
    {
        if (hotelDTO == null)
        {
            return error.createHotelErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }
        if (validation.isInvalidHotelData(hotelDTO))
        {
            return error.createHotelErrorResponse("Invalid hotel data", HttpStatus.BAD_REQUEST);
        }
        try
        {
            HotelDTO updatedHotel = hotelService.updateHotel(hotelID, hotelDTO);
            if (updatedHotel != null)
            {
                return ResponseEntity.ok(updatedHotel);
            }
            return error.createHotelErrorResponse("Hotel not found for update", HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while updating hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to delete a hotel by ID
    @DeleteMapping("admin/hotel/delete/{hotelID}")
    public ResponseEntity<HotelDTO> deleteHotel(@PathVariable Long hotelID)
    {
        try
        {
            hotelService.deleteHotel(hotelID);
            return ResponseEntity.noContent().build();
        } catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while deleting hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}