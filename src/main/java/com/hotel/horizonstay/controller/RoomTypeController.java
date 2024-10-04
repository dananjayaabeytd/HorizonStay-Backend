package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomtype")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping("/{seasonID}")
    public ResponseEntity<RoomTypeDTO> addRoomTypeToSeason(@PathVariable Long seasonID, @RequestBody RoomTypeDTO roomTypeDTO) {
        try {
            RoomTypeDTO createdRoomType = roomTypeService.addRoomTypeToSeason(seasonID, roomTypeDTO);
            return new ResponseEntity<>(createdRoomType, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error.createRoomTypeErrorResponse("Error occurred while adding room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomTypeID}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Long roomTypeID) {
        try {
            RoomTypeDTO roomTypeDTO = roomTypeService.getRoomTypeById(roomTypeID);
            return new ResponseEntity<>(roomTypeDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createRoomTypeErrorResponse("Error occurred while fetching room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{roomTypeID}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Long roomTypeID, @RequestBody RoomTypeDTO roomTypeDTO) {
        try {
            RoomTypeDTO updatedRoomType = roomTypeService.updateRoomType(roomTypeID, roomTypeDTO);
            return new ResponseEntity<>(updatedRoomType, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createRoomTypeErrorResponse("Error occurred while updating room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{roomTypeID}")
    public ResponseEntity<RoomTypeDTO> deleteRoomType(@PathVariable Long roomTypeID) {
        try {
            roomTypeService.deleteRoomType(roomTypeID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createRoomTypeErrorResponse("Error occurred while deleting room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/season/{seasonID}")
    public ResponseEntity<List<RoomTypeDTO>> getRoomTypesBySeasonId(@PathVariable Long seasonID) {
        try {
            List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypesBySeasonId(seasonID);
            return ResponseEntity.ok(roomTypes);
        } catch (Exception e) {
            return error.createRoomtErrorResponseList("Error occurred while fetching room types", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}