package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.DiscountDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping("/{seasonID}")
    public ResponseEntity<DiscountDTO> addDiscountToSeason(@PathVariable Long seasonID, @RequestBody DiscountDTO discountDTO) {
        try {
            DiscountDTO createdDiscount = discountService.addDiscountToSeason(seasonID, discountDTO);
            return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return error.createDiscountErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return error.createDiscountErrorResponse("Error occurred while adding discount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{discountID}")
    public ResponseEntity<DiscountDTO> getDiscountById(@PathVariable Long discountID) {
        try {
            DiscountDTO discountDTO = discountService.getDiscountById(discountID);
            return new ResponseEntity<>(discountDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createDiscountErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createDiscountErrorResponse("Error occurred while fetching discount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{discountID}")
    public ResponseEntity<DiscountDTO> updateDiscount(@PathVariable Long discountID, @RequestBody DiscountDTO discountDTO) {
        try {
            DiscountDTO updatedDiscount = discountService.updateDiscount(discountID, discountDTO);
            return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createDiscountErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createDiscountErrorResponse("Error occurred while updating discount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{discountID}")
    public ResponseEntity<DiscountDTO> deleteDiscount(@PathVariable Long discountID) {
        try {
            discountService.deleteDiscount(discountID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return error.createDiscountErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createDiscountErrorResponse("Error occurred while deleting discount", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}