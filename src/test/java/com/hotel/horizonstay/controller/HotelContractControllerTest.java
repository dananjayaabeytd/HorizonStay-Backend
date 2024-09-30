package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.*;
import com.hotel.horizonstay.service.HotelContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class HotelContractControllerTest
{

    @Mock
    private HotelContractService hotelContractService;

    @InjectMocks
    private HotelContractController hotelContractController;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for successful contract addition
    @Test
    void testAddContract_Success()
    {
        // Create a sample HotelContractDTO object
        HotelContractDTO contractDTO = createValidContractDTO();

        // Mock the addHotelContract method of hotelContractService to return the sample HotelContractDTO
        when(hotelContractService.addHotelContract(anyLong(), any(HotelContractDTO.class))).thenReturn(contractDTO);

        // Call the addContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.addContract(1L, contractDTO);

        // Assert the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // Test case for invalid hotel ID
    @Test
    void testAddContract_InvalidHotelID()
    {
        // Mock the addHotelContract method of hotelContractService to throw an IllegalArgumentException
        when(hotelContractService.addHotelContract(anyLong(), any(HotelContractDTO.class))).thenThrow(new IllegalArgumentException("Hotel not found"));

        // Call the addContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.addContract(1L, createValidContractDTO());

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Hotel not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for null request body
    @Test
    void testAddContract_NullRequestBody()
    {
        // Call the addContract method with null request body
        ResponseEntity<HotelContractDTO> response = hotelContractController.addContract(1L, null);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for invalid contract dates
    @Test
    void testAddContract_InvalidDates()
    {
        // Create a HotelContractDTO object with invalid dates
        HotelContractDTO contractDTO = createValidContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2025, 9, 30));
        contractDTO.setValidTo(LocalDate.of(2025, 6, 1));

        // Call the addContract method with invalid dates
        ResponseEntity<HotelContractDTO> response = hotelContractController.addContract(1L, contractDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Contract validFrom date must be before validTo date", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for season dates outside contract dates
    @Test
    void testAddContract_SeasonDatesOutsideContractDates()
    {
        // Create a HotelContractDTO object with season dates outside contract dates
        HotelContractDTO contractDTO = createValidContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2025, 6, 1));
        contractDTO.setValidTo(LocalDate.of(2025, 9, 30));

        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setValidFrom(LocalDate.of(2025, 5, 1));
        seasonDTO.setValidTo(LocalDate.of(2025, 8, 31));
        contractDTO.setSeasons(Collections.singletonList(seasonDTO));

        // Call the addContract method with season dates outside contract dates
        ResponseEntity<HotelContractDTO> response = hotelContractController.addContract(1L, contractDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Season dates must lie within the contract dates", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for exception during contract addition
    @Test
    void testAddContract_Exception()
    {
        // Create a sample HotelContractDTO object
        HotelContractDTO contractDTO = createValidContractDTO();

        // Mock the addHotelContract method of hotelContractService to throw an exception
        when(hotelContractService.addHotelContract(anyLong(), any(HotelContractDTO.class))).thenThrow(new RuntimeException("Error"));

        // Call the addContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.addContract(1L, contractDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding contract", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for successful contract retrieval by ID
    @Test
    void testGetContractById_Success()
    {
        // Create a sample HotelContractDTO object
        HotelContractDTO contractDTO = createValidContractDTO();

        // Mock the getContractById method of hotelContractService to return the sample HotelContractDTO
        when(hotelContractService.getContractById(anyLong())).thenReturn(contractDTO);

        // Call the getContractById method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.getContractById(1L);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Test case for contract not found by ID
    @Test
    void testGetContractById_NotFound()
    {
        // Mock the getContractById method of hotelContractService to throw an IllegalArgumentException
        when(hotelContractService.getContractById(anyLong())).thenThrow(new IllegalArgumentException("Contract not found"));

        // Call the getContractById method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.getContractById(1L);

        // Assert the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Contract not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for exception during contract retrieval by ID
    @Test
    void testGetContractById_Exception()
    {
        // Mock the getContractById method of hotelContractService to throw an exception
        when(hotelContractService.getContractById(anyLong())).thenThrow(new RuntimeException("Error"));

        // Call the getContractById method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.getContractById(1L);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching contract", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for successful contract update
    @Test
    void testUpdateContract_Success()
    {
        // Create a sample HotelContractDTO object
        HotelContractDTO contractDTO = createValidContractDTO();

        // Mock the updateContract method of hotelContractService to return the sample HotelContractDTO
        when(hotelContractService.updateContract(anyLong(), any(HotelContractDTO.class))).thenReturn(contractDTO);

        // Call the updateContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.updateContract(1L, contractDTO);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Test case for contract not found during update
    @Test
    void testUpdateContract_NotFound()
    {
        // Mock the updateContract method of hotelContractService to throw an IllegalArgumentException
        when(hotelContractService.updateContract(anyLong(), any(HotelContractDTO.class))).thenThrow(new IllegalArgumentException("Contract not found"));

        // Call the updateContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.updateContract(1L, createValidContractDTO());

        // Assert the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Contract not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for invalid contract dates during update
    @Test
    void testUpdateContract_InvalidDates()
    {
        // Create a HotelContractDTO object with invalid dates
        HotelContractDTO contractDTO = createValidContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2025, 9, 30));
        contractDTO.setValidTo(LocalDate.of(2025, 6, 1)); // Invalid dates

        // Call the updateContract method with invalid dates
        ResponseEntity<HotelContractDTO> response = hotelContractController.updateContract(1L, contractDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Contract validFrom date must be before validTo date", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for season dates outside contract dates during update
    @Test
    void testUpdateContract_SeasonDatesOutsideContractDates()
    {
        // Create a HotelContractDTO object with season dates outside contract dates
        HotelContractDTO contractDTO = createValidContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2025, 6, 1));
        contractDTO.setValidTo(LocalDate.of(2025, 9, 30));

        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setValidFrom(LocalDate.of(2025, 5, 1)); // Outside contract dates
        seasonDTO.setValidTo(LocalDate.of(2025, 8, 31));
        contractDTO.setSeasons(Collections.singletonList(seasonDTO));

        // Call the updateContract method with season dates outside contract dates
        ResponseEntity<HotelContractDTO> response = hotelContractController.updateContract(1L, contractDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Season dates must lie within the contract dates", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for exception during contract update
    @Test
    void testUpdateContract_Exception()
    {
        // Create a sample HotelContractDTO object
        HotelContractDTO contractDTO = createValidContractDTO();

        // Mock the updateContract method of hotelContractService to throw an exception
        when(hotelContractService.updateContract(anyLong(), any(HotelContractDTO.class))).thenThrow(new RuntimeException("Error"));

        // Call the updateContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.updateContract(1L, contractDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating contract", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for successful contract deletion
    @Test
    void testDeleteContract_Success()
    {
        // Call the deleteContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.deleteContract(1L);

        // Assert the response status
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Test case for contract not found during deletion
    @Test
    void testDeleteContract_NotFound()
    {
        // Mock the deleteContract method of hotelContractService to throw an IllegalArgumentException
        doThrow(new IllegalArgumentException("Contract not found")).when(hotelContractService).deleteContract(anyLong());

        // Call the deleteContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.deleteContract(1L);

        // Assert the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Contract not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for exception during contract deletion
    @Test
    void testDeleteContract_Exception()
    {
        // Mock the deleteContract method of hotelContractService to throw an exception
        doThrow(new RuntimeException("Error")).when(hotelContractService).deleteContract(anyLong());

        // Call the deleteContract method of hotelContractController
        ResponseEntity<HotelContractDTO> response = hotelContractController.deleteContract(1L);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting contract", Objects.requireNonNull(response.getBody()).getMessage());
    }

    // Test case for successful hotel contract search
//    @Test
//    void testSearchHotelContracts_Success()
//    {
//        // Create a sample list of HotelContractDTO objects
//        List<SearchResultDTO> contracts = Collections.singletonList(createValidContractDTO());
//
//        // Mock the searchHotelContracts method of hotelContractService to return the sample list
//        when(hotelContractService.searchHotelContracts(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(contracts);
//
//        // Call the searchHotelContracts method of hotelContractController
//        ResponseEntity<List<SearchResultDTO>> response = hotelContractController.searchHotelContracts("location", "2023-01-01", "2023-01-10", 2, 2);
//
//        // Assert the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(1, response.getBody().size());
//    }

    // Test case for invalid date format during hotel contract search
    @Test
    void testSearchHotelContracts_InvalidDateFormat()
    {
        // Mock the searchHotelContracts method of hotelContractService to throw an IllegalArgumentException
        when(hotelContractService.searchHotelContracts(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenThrow(new IllegalArgumentException("Invalid date format"));

        // Call the searchHotelContracts method of hotelContractController
        ResponseEntity<List<SearchResultDTO>> response = hotelContractController.searchHotelContracts("location", "invalid-date", "2023-01-10", 2, 2);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date format", Objects.requireNonNull(response.getBody()).get(0).getMessage());
    }

    // Test case for exception during hotel contract search
    @Test
    void testSearchHotelContracts_Exception()
    {
        // Mock the searchHotelContracts method of hotelContractService to throw an exception
        when(hotelContractService.searchHotelContracts(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenThrow(new RuntimeException("Error"));

        // Call the searchHotelContracts method of hotelContractController
        ResponseEntity<List<SearchResultDTO>> response = hotelContractController.searchHotelContracts("location", "2023-01-01", "2023-01-10", 2, 2);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while searching contracts", Objects.requireNonNull(response.getBody()).get(0).getMessage());
    }

    // Helper method to create a valid HotelContractDTO
    private HotelContractDTO createValidContractDTO()
    {
        HotelContractDTO contractDTO = new HotelContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2025, 6, 1));
        contractDTO.setValidTo(LocalDate.of(2025, 9, 30));
        contractDTO.setCancellationPolicy("Non-refundable");
        contractDTO.setPaymentPolicy("Full payment upon booking");

        SeasonDTO summerSeason = new SeasonDTO();
        summerSeason.setSeasonName("Summer");
        summerSeason.setValidFrom(LocalDate.of(2025, 6, 1));
        summerSeason.setValidTo(LocalDate.of(2025, 8, 31));
        summerSeason.setRoomTypes(List.of(
                createRoomTypeDTO("Deluxe Room", 10, 3, 150.0, List.of("deluxe-room-image1.jpg", "deluxe-room-image2.jpg")),
                createRoomTypeDTO("Suite", 5, 5, 250.0, List.of("suite-image1.jpg", "suite-image2.jpg"))
        ));
        summerSeason.setSupplements(List.of(
                createSupplementDTO("Breakfast", 20.0)
        ));
        summerSeason.setDiscounts(List.of(
                createDiscountDTO("Early Bird", 10.0)
        ));
        summerSeason.setMarkups(List.of(
                createMarkupDTO("Service Charge", 5.0)
        ));

        SeasonDTO autumnSeason = new SeasonDTO();
        autumnSeason.setSeasonName("Autumn");
        autumnSeason.setValidFrom(LocalDate.of(2025, 9, 1));
        autumnSeason.setValidTo(LocalDate.of(2025, 9, 30));
        autumnSeason.setRoomTypes(List.of(
                createRoomTypeDTO("Standard Room", 15, 2, 120.0, List.of("standard-room-image1.jpg", "standard-room-image2.jpg"))
        ));
        autumnSeason.setSupplements(List.of(
                createSupplementDTO("Parking", 15.0)
        ));
        autumnSeason.setDiscounts(List.of(
                createDiscountDTO("Weekend Special", 15.0)
        ));
        autumnSeason.setMarkups(List.of(
                createMarkupDTO("Booking Fee", 10.0)
        ));

        contractDTO.setSeasons(List.of(summerSeason, autumnSeason));
        return contractDTO;
    }


    // Helper method to create a RoomTypeDTO
    private RoomTypeDTO createRoomTypeDTO(String name, int numberOfRooms, int maxPersons, double price, List<String> images)
    {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName(name);
        roomTypeDTO.setNumberOfRooms(numberOfRooms);
        roomTypeDTO.setMaxNumberOfPersons(maxPersons);
        roomTypeDTO.setPrice((float) price);
        roomTypeDTO.setRoomTypeImages(images);
        return roomTypeDTO;
    }

    // Helper method to create a SupplementDTO
    private SupplementDTO createSupplementDTO(String name, double price)
    {
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName(name);
        supplementDTO.setPrice((float) price);
        return supplementDTO;
    }

    // Helper method to create a DiscountDTO
    private DiscountDTO createDiscountDTO(String name, double percentage)
    {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName(name);
        discountDTO.setPercentage((float) percentage);
        return discountDTO;
    }

    // Helper method to create a MarkupDTO
    private MarkupDTO createMarkupDTO(String name, double percentage)
    {
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName(name);
        markupDTO.setPercentage(percentage);
        return markupDTO;
    }
}