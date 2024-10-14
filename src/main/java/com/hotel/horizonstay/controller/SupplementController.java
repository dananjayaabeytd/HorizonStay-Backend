package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.SupplementDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.service.SupplementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplement")
public class SupplementController {

    @Autowired
    private SupplementService supplementService;

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping("/{seasonID}")
    public ResponseEntity<SupplementDTO> addSupplementToSeason(@PathVariable Long seasonID, @RequestBody SupplementDTO supplementDTO) {
        try {
            // Validate supplementDTO
            if (supplementDTO.getSupplementName() == null || supplementDTO.getSupplementName().isEmpty()) {
                return error.createSupplementErrorResponse("Invalid supplement data", HttpStatus.BAD_REQUEST);
            }

            SupplementDTO createdSupplement = supplementService.addSupplementToSeason(seasonID, supplementDTO);
            return new ResponseEntity<>(createdSupplement, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error.createSupplementErrorResponse("Error occurred while adding supplement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/{seasonID}")
//    public ResponseEntity<SupplementDTO> addSupplementToSeason(@PathVariable Long seasonID, @RequestBody SupplementDTO supplementDTO) {
//        try {
//            SupplementDTO createdSupplement = supplementService.addSupplementToSeason(seasonID, supplementDTO);
//            return new ResponseEntity<>(createdSupplement, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return error.createSupplementErrorResponse("Error occurred while adding supplement", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{supplementID}")
    public ResponseEntity<SupplementDTO> getSupplementById(@PathVariable Long supplementID) {
        try {
            SupplementDTO supplementDTO = supplementService.getSupplementById(supplementID);
            if (supplementDTO == null) {
                return error.createSupplementErrorResponse("Supplement not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(supplementDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createSupplementErrorResponse("Error occurred while fetching supplement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/{supplementID}")
//    public ResponseEntity<SupplementDTO> getSupplementById(@PathVariable Long supplementID) {
//        try {
//            SupplementDTO supplementDTO = supplementService.getSupplementById(supplementID);
//            return new ResponseEntity<>(supplementDTO, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return error.createSupplementErrorResponse("Error occurred while fetching supplement", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping("/update/{supplementID}")
    public ResponseEntity<SupplementDTO> updateSupplement(@PathVariable Long supplementID, @RequestBody SupplementDTO supplementDTO) {
        try {
            SupplementDTO updatedSupplement = supplementService.updateSupplement(supplementID, supplementDTO);
            if (updatedSupplement == null) {
                return error.createSupplementErrorResponse("Supplement not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedSupplement, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createSupplementErrorResponse("Error occurred while updating supplement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/update/{supplementID}")
//    public ResponseEntity<SupplementDTO> updateSupplement(@PathVariable Long supplementID, @RequestBody SupplementDTO supplementDTO) {
//        try {
//            SupplementDTO updatedSupplement = supplementService.updateSupplement(supplementID, supplementDTO);
//            return new ResponseEntity<>(updatedSupplement, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return error.createSupplementErrorResponse("Error occurred while updating supplement", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/delete/{supplementID}")
    public ResponseEntity<SupplementDTO> deleteSupplement(@PathVariable Long supplementID) {
        try {
            supplementService.deleteSupplement(supplementID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createSupplementErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createSupplementErrorResponse("Error occurred while deleting supplement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/season/{seasonID}")
    public ResponseEntity<List<SupplementDTO>> getSupplementsBySeasonId(@PathVariable Long seasonID) {
        try {
            List<SupplementDTO> supplements = supplementService.getSupplementsBySeasonId(seasonID);
            return new ResponseEntity<>(supplements, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}