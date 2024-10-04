package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.SeasonDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping("/{contractID}")
    public ResponseEntity<SeasonDTO> addSeasonToContract(@PathVariable Long contractID, @RequestBody SeasonDTO seasonDTO) {
        try {
            SeasonDTO createdSeason = seasonService.addSeasonToContract(contractID, seasonDTO);
            return new ResponseEntity<>(createdSeason, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return error.createSeasonErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error.createSeasonErrorResponse("Error occurred while adding season", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{seasonID}")
    public ResponseEntity<SeasonDTO> getSeasonById(@PathVariable Long seasonID) {
        try {
            SeasonDTO seasonDTO = seasonService.getSeasonById(seasonID);
            return new ResponseEntity<>(seasonDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createSeasonErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createSeasonErrorResponse("Error occurred while fetching season", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{seasonID}")
    public ResponseEntity<SeasonDTO> updateSeason(@PathVariable Long seasonID, @RequestBody SeasonDTO seasonDTO) {
        try {
            SeasonDTO updatedSeason = seasonService.updateSeason(seasonID, seasonDTO);
            return new ResponseEntity<>(updatedSeason, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createSeasonErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createSeasonErrorResponse("Error occurred while updating season", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{seasonID}")
    public ResponseEntity<SeasonDTO> deleteSeason(@PathVariable Long seasonID) {
        try {
            seasonService.deleteSeason(seasonID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createSeasonErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createSeasonErrorResponse("Error occurred while deleting season", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/contract/{contractID}")
    public ResponseEntity<List<SeasonDTO>> getSeasonsByContractId(@PathVariable Long contractID) {
        try {
            List<SeasonDTO> seasons = seasonService.getSeasonsByContractId(contractID);
            return ResponseEntity.ok(seasons);
        } catch (Exception e) {
            return error.createSeasonErrorResponseList("Error occurred while fetching seasons", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}