package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.HotelContractDTO;
import com.hotel.horizonstay.dto.SearchResultDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.helper.Validation;
import com.hotel.horizonstay.service.HotelContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class HotelContractController {

    @Autowired
    private HotelContractService hotelContractService;

    private final Validation validation = new Validation();

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping("admin/contract/{hotelID}")
    public ResponseEntity<HotelContractDTO> addContract(@PathVariable Long hotelID, @RequestBody HotelContractDTO contractDTO)
    {
        if (validateContractData(contractDTO) != null) {
            return validateContractData(contractDTO);
        }
        try
        {
            HotelContractDTO createdContract = hotelContractService.addHotelContract(hotelID, contractDTO);
            return new ResponseEntity<>(createdContract, HttpStatus.CREATED);

        } catch (IllegalArgumentException e)
        {
            return error.createContractErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e)
        {
            return error.createContractErrorResponse("Error occurred while adding contract", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("admin/contract/{contractID}")
    public ResponseEntity<HotelContractDTO> getContractById(@PathVariable Long contractID)
    {
        try
        {
            HotelContractDTO contractDTO = hotelContractService.getContractById(contractID);
            return new ResponseEntity<>(contractDTO, HttpStatus.OK);

        } catch (IllegalArgumentException e)
        {
            return error.createContractErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e)
        {
            return error.createContractErrorResponse("Error occurred while fetching contract", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("admin/contract/update/{contractID}")
    public ResponseEntity<HotelContractDTO> updateContract(@PathVariable Long contractID, @RequestBody HotelContractDTO contractDTO)
    {
        if (validateContractData(contractDTO) != null) {
            return validateContractData(contractDTO);
        }

        try
        {
            HotelContractDTO updatedContract = hotelContractService.updateContract(contractID, contractDTO);
            return new ResponseEntity<>(updatedContract, HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return error.createContractErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);

        }
        catch (Exception e)
        {
            return error.createContractErrorResponse("Error occurred while updating contract", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("admin/contract/delete/{contractID}")
    public ResponseEntity<HotelContractDTO> deleteContract(@PathVariable Long contractID)
    {
        try
        {
            hotelContractService.deleteContract(contractID);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return error.createContractErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createContractErrorResponse("Error occurred while deleting contract", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchResultDTO>> searchHotelContracts(@RequestParam String location, @RequestParam String checkInDate, @RequestParam String checkOutDate, @RequestParam int adults, @RequestParam int children)
    {
        try
        {
            List<SearchResultDTO> matchingContracts = hotelContractService.searchHotelContracts(location, checkInDate, checkOutDate, adults, children);
            return ResponseEntity.ok(matchingContracts);
        }
        catch (IllegalArgumentException e)
        {
            return error.createSearchErrorResponseList(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            return error.createSearchErrorResponseList("Error occurred while searching contracts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<HotelContractDTO> validateContractData(HotelContractDTO contractDTO) {
        if (contractDTO == null) {
            return error.createContractErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }
        if (validation.isInvalidContractData(contractDTO)) {
            return error.createContractErrorResponse("Invalid contract data", HttpStatus.BAD_REQUEST);
        }
        if (!contractDTO.getValidFrom().isBefore(contractDTO.getValidTo())) {
            return error.createContractErrorResponse("Contract validFrom date must be before validTo date", HttpStatus.BAD_REQUEST);
        }
        for (var season : contractDTO.getSeasons()) {
            if (!season.getValidFrom().isBefore(season.getValidTo())) {
                return error.createContractErrorResponse("Season validFrom date must be before validTo date", HttpStatus.BAD_REQUEST);
            }
            if (season.getValidFrom().isBefore(contractDTO.getValidFrom()) || season.getValidTo().isAfter(contractDTO.getValidTo())) {
                return error.createContractErrorResponse("Season dates must lie within the contract dates", HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    @GetMapping("admin/contract/hotel/{hotelID}")
    public ResponseEntity<List<HotelContractDTO>> getContractsByHotelId(@PathVariable Long hotelID) {
        try {
            List<HotelContractDTO> contracts = hotelContractService.getContractsByHotelId(hotelID);
            return ResponseEntity.ok(contracts);
        } catch (Exception e) {
            return error.createErrorResponseList("Error occurred while fetching contracts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
