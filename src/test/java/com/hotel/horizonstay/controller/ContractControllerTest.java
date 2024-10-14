package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.CalculationDTO;
import com.hotel.horizonstay.dto.HotelContractDTO;
import com.hotel.horizonstay.dto.SearchResultDTO;
import com.hotel.horizonstay.helper.Validation;
import com.hotel.horizonstay.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class ContractControllerTest {

    @Mock
    private ContractService hotelContractService;

    @Mock
    private Validation validation;

    @InjectMocks
    private ContractController contractController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddContract_Success()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        when(hotelContractService.addHotelContract(anyLong(), any(HotelContractDTO.class))).thenReturn(contractDTO);

        ResponseEntity<HotelContractDTO> response = contractController.addContract(1L, contractDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(contractDTO, response.getBody());
    }

    @Test
    void testAddContract_NullRequestBody()
    {
        ResponseEntity<HotelContractDTO> response = contractController.addContract(1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", response.getBody().getMessage());
    }

    @Test
    void testAddContract_ValidFromAfterValidTo()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2024, 5, 1));
        contractDTO.setValidTo(LocalDate.of(2024, 4, 1));

        ResponseEntity<HotelContractDTO> response = contractController.addContract(1L, contractDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Contract validFrom date must be before validTo date", response.getBody().getMessage());
    }

    @Test
    void testAddContract_Exception()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        when(hotelContractService.addHotelContract(anyLong(), any(HotelContractDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<HotelContractDTO> response = contractController.addContract(1L, contractDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding contract", response.getBody().getMessage());
    }

    @Test
    void testGetContractById_Success()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        when(hotelContractService.getContractById(anyLong())).thenReturn(contractDTO);

        ResponseEntity<HotelContractDTO> response = contractController.getContractById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contractDTO, response.getBody());
    }

    @Test
    void testGetContractById_InvalidArgument()
    {
        when(hotelContractService.getContractById(anyLong())).thenThrow(new IllegalArgumentException("Invalid ID"));

        ResponseEntity<HotelContractDTO> response = contractController.getContractById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody().getMessage());
    }

    @Test
    void testGetContractById_Exception()
    {
        when(hotelContractService.getContractById(anyLong())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<HotelContractDTO> response = contractController.getContractById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching contract", response.getBody().getMessage());
    }

    @Test
    void testUpdateContract_Success()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        when(hotelContractService.updateContract(anyLong(), any(HotelContractDTO.class))).thenReturn(contractDTO);

        ResponseEntity<HotelContractDTO> response = contractController.updateContract(1L, contractDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contractDTO, response.getBody());
    }

    @Test
    void testUpdateContract_NullRequestBody()
    {
        ResponseEntity<HotelContractDTO> response = contractController.updateContract(1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", response.getBody().getMessage());
    }

    @Test
    void testUpdateContract_ValidFromAfterValidTo()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2024, 5, 1));
        contractDTO.setValidTo(LocalDate.of(2024, 4, 1));

        ResponseEntity<HotelContractDTO> response = contractController.updateContract(1L, contractDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Contract validFrom date must be before validTo date", response.getBody().getMessage());
    }

    @Test
    void testUpdateContract_Exception()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        when(hotelContractService.updateContract(anyLong(), any(HotelContractDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<HotelContractDTO> response = contractController.updateContract(1L, contractDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating contract", response.getBody().getMessage());
    }

    @Test
    void testDeleteContract_Success()
    {
        HotelContractDTO contractDTO = createValidContractDTO();
        when(hotelContractService.deleteContract(anyLong())).thenReturn(contractDTO);

        ResponseEntity<HotelContractDTO> response = contractController.deleteContract(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contractDTO, response.getBody());
    }

    @Test
    void testDeleteContract_InvalidArgument()
    {
        doThrow(new IllegalArgumentException("Invalid ID")).when(hotelContractService).deleteContract(anyLong());

        ResponseEntity<HotelContractDTO> response = contractController.deleteContract(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody().getMessage());
    }

    @Test
    void testDeleteContract_Exception()
    {
        doThrow(new RuntimeException("Error")).when(hotelContractService).deleteContract(anyLong());

        ResponseEntity<HotelContractDTO> response = contractController.deleteContract(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting contract", response.getBody().getMessage());
    }

    @Test
    void testGetContractsByHotelId_Success()
    {
        List<HotelContractDTO> contracts = Collections.singletonList(createValidContractDTO());
        when(hotelContractService.getContractsByHotelId(anyLong())).thenReturn(contracts);

        ResponseEntity<List<HotelContractDTO>> response = contractController.getContractsByHotelId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contracts, response.getBody());
    }

    @Test
    void testGetContractsByHotelId_Exception()
    {
        when(hotelContractService.getContractsByHotelId(anyLong())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<HotelContractDTO>> response = contractController.getContractsByHotelId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching contracts", response.getBody().get(0).getMessage());
    }

    @Test
    void testSearchHotelContracts_Success()
    {
        List<SearchResultDTO> searchResults = Collections.singletonList(new SearchResultDTO());
        when(hotelContractService.searchHotelContracts(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(searchResults);

        ResponseEntity<List<SearchResultDTO>> response = contractController.searchHotelContracts("location", "2023-10-01", "2023-10-10", 2, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(searchResults, response.getBody());
    }

    @Test
    void testSearchHotelContracts_InvalidArgument()
    {
        when(hotelContractService.searchHotelContracts(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenThrow(new IllegalArgumentException("Invalid search parameters"));

        ResponseEntity<List<SearchResultDTO>> response = contractController.searchHotelContracts("location", "2023-10-01", "2023-10-10", 2, 1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid search parameters", response.getBody().get(0).getMessage());
    }

    @Test
    void testSearchHotelContracts_Exception()
    {
        when(hotelContractService.searchHotelContracts(anyString(), anyString(), anyString(), anyInt(), anyInt())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<SearchResultDTO>> response = contractController.searchHotelContracts("location", "2023-10-01", "2023-10-10", 2, 1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while searching contracts", response.getBody().get(0).getMessage());
    }

    @Test
    void testCalculateAmount_Success()
    {
        CalculationDTO calculationDTO = createCalculationDTO();
        Map<String, Float> result = new HashMap<>();
        result.put("totalAmount", 1000.0f);
        result.put("discountAmount", 200.0f);
        when(hotelContractService.calculatePayableAmount(any(CalculationDTO.class))).thenReturn(result);

        ResponseEntity<Map<String, Object>> response = contractController.calculateAmount(calculationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1000.0f, response.getBody().get("payableAmount"));
        assertEquals(200.0f, response.getBody().get("discountAmount"));
    }

    @Test
    void testCalculateAmount_NullRequestBody()
    {
        ResponseEntity<Map<String, Object>> response = contractController.calculateAmount(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", response.getBody().get("error"));
    }

    @Test
    void testCalculateAmount_InvalidDateFormat()
    {
        CalculationDTO calculationDTO = createCalculationDTO();
        calculationDTO.setCheckIn("invalid-date");

        ResponseEntity<Map<String, Object>> response = contractController.calculateAmount(calculationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date format. Please use YYYY-MM-DD.", response.getBody().get("error"));
    }

    @Test
    void testCalculateAmount_CheckInAfterCheckOut()
    {
        CalculationDTO calculationDTO = createCalculationDTO();
        calculationDTO.setCheckIn("2025-12-16");

        ResponseEntity<Map<String, Object>> response = contractController.calculateAmount(calculationDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Check-in date must be before check-out date.", response.getBody().get("error"));
    }

    @Test
    void testCalculateAmount_Exception()
    {
        CalculationDTO calculationDTO = createCalculationDTO();
        when(hotelContractService.calculatePayableAmount(any(CalculationDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Map<String, Object>> response = contractController.calculateAmount(calculationDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while calculating the amount", response.getBody().get("error"));
    }

    private HotelContractDTO createValidContractDTO() {
        HotelContractDTO contractDTO = new HotelContractDTO();
        contractDTO.setValidFrom(LocalDate.of(2023, 10, 1));
        contractDTO.setValidTo(LocalDate.of(2024, 4, 1));
        contractDTO.setCancellationPolicy("Free cancellation within 24 hours");
        contractDTO.setPaymentPolicy("Full payment required upon booking");
        return contractDTO;
    }

    private CalculationDTO createCalculationDTO() {
        CalculationDTO calculationDTO = new CalculationDTO();
        calculationDTO.setCheckIn("2025-11-11");
        calculationDTO.setCheckOut("2025-12-15");
        calculationDTO.setNoOfAdults(2);
        calculationDTO.setNoOfChildren(1);
        calculationDTO.setDiscountPercentage(20.0f);
        calculationDTO.setMarkupPercentage(10.0f);
        return calculationDTO;
    }

    //    @Test
//    void testAddContract_InvalidContractData() {
//        HotelContractDTO contractDTO = createValidContractDTO();
//        when(validation.isInvalidContractData(any(HotelContractDTO.class))).thenReturn(true);
//
//        ResponseEntity<HotelContractDTO> response = contractController.addContract(1L, contractDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Invalid contract data", response.getBody().getMessage());
//    }


//    @Test
//    void testUpdateContract_InvalidContractData() {
//        HotelContractDTO contractDTO = createValidContractDTO();
//        when(validation.isInvalidContractData(any(HotelContractDTO.class))).thenReturn(true);
//
//        ResponseEntity<HotelContractDTO> response = contractController.updateContract(1L, contractDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Invalid contract data", response.getBody().getMessage());
//    }
}