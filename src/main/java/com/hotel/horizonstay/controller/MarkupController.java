package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.MarkupDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.service.MarkupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/markup")
public class MarkupController {

    @Autowired
    private MarkupService markupService;

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping("/{seasonID}")
    public ResponseEntity<MarkupDTO> addMarkupToSeason(@PathVariable Long seasonID, @RequestBody MarkupDTO markupDTO) {
        try {
            MarkupDTO createdMarkup = markupService.addMarkupToSeason(seasonID, markupDTO);
            return new ResponseEntity<>(createdMarkup, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return error.createMarkupErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error.createMarkupErrorResponse("Error occurred while adding markup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{markupID}")
    public ResponseEntity<MarkupDTO> getMarkupById(@PathVariable Long markupID) {
        try {
            MarkupDTO markupDTO = markupService.getMarkupById(markupID);
            if (markupDTO == null) {
                return error.createMarkupErrorResponse("Markup not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(markupDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createMarkupErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createMarkupErrorResponse("Error occurred while fetching markup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/{markupID}")
//    public ResponseEntity<MarkupDTO> getMarkupById(@PathVariable Long markupID) {
//        try {
//            MarkupDTO markupDTO = markupService.getMarkupById(markupID);
//            return new ResponseEntity<>(markupDTO, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return error.createMarkupErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return error.createMarkupErrorResponse("Error occurred while fetching markup", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping("/update/{markupID}")
    public ResponseEntity<MarkupDTO> updateMarkup(@PathVariable Long markupID, @RequestBody MarkupDTO markupDTO) {
        try {
            MarkupDTO updatedMarkup = markupService.updateMarkup(markupID, markupDTO);
            if (updatedMarkup == null) {
                return error.createMarkupErrorResponse("Markup not found for update", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedMarkup, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createMarkupErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createMarkupErrorResponse("Error occurred while updating markup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/update/{markupID}")
//    public ResponseEntity<MarkupDTO> updateMarkup(@PathVariable Long markupID, @RequestBody MarkupDTO markupDTO) {
//        try {
//            MarkupDTO updatedMarkup = markupService.updateMarkup(markupID, markupDTO);
//            return new ResponseEntity<>(updatedMarkup, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return error.createMarkupErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return error.createMarkupErrorResponse("Error occurred while updating markup", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/delete/{markupID}")
    public ResponseEntity<MarkupDTO> deleteMarkup(@PathVariable Long markupID) {
        try {
            markupService.deleteMarkup(markupID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createMarkupErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createMarkupErrorResponse("Error occurred while deleting markup", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/season/{seasonID}")
    public ResponseEntity<List<MarkupDTO>> getMarkupsBySeasonId(@PathVariable Long seasonID) {
        try {
            List<MarkupDTO> markups = markupService.getMarkupsBySeasonId(seasonID);
            return new ResponseEntity<>(markups, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}