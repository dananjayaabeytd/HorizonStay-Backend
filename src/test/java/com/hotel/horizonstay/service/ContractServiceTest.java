package com.hotel.horizonstay.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hotel.horizonstay.dto.HotelContractDTO;
import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.repository.ContractRepository;
import com.hotel.horizonstay.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ContractService contractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addHotelContract_Success() {
        HotelContractDTO contractDTO = new HotelContractDTO();
        contractDTO.setValidFrom(LocalDate.from(LocalDateTime.now()));
        contractDTO.setValidTo(LocalDate.from(LocalDateTime.now().plusDays(10)));
        contractDTO.setCancellationPolicy("Free cancellation");
        contractDTO.setPaymentPolicy("Prepaid");

        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        hotel.setHotelName("Test Hotel"); // Ensure hotel name is set
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelContract contract = new HotelContract();
        contract.setId(1L);
        contract.setHotel(hotel); // Ensure hotel is set in contract
        when(contractRepository.save(any())).thenReturn(contract);

        HotelContractDTO result = contractService.addHotelContract(1L, contractDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Hotel contract added successfully", result.getMessage());
    }

    @Test
    void addHotelContract_HotelNotFound() {
        HotelContractDTO contractDTO = new HotelContractDTO();
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        HotelContractDTO result = contractService.addHotelContract(1L, contractDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Hotel not found", result.getMessage());
    }

    @Test
    void getContractById_Success() {
        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        hotel.setHotelName("Test Hotel");

        HotelContract contract = new HotelContract();
        contract.setId(1L);
        contract.setHotel(hotel);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        HotelContractDTO result = contractService.getContractById(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Contract found successfully", result.getMessage());
    }

    @Test
    void getContractById_NotFound() {
        when(contractRepository.findById(1L)).thenReturn(Optional.empty());

        HotelContractDTO result = contractService.getContractById(1L);

        assertEquals(404, result.getStatusCode());
        assertEquals("Contract not found", result.getMessage());
    }

    @Test
    void updateContract_Success() {
        HotelContractDTO contractDTO = new HotelContractDTO();
        contractDTO.setValidFrom(LocalDate.from(LocalDateTime.now()));
        contractDTO.setValidTo(LocalDate.from(LocalDateTime.now().plusDays(10)));
        contractDTO.setCancellationPolicy("Free cancellation");
        contractDTO.setPaymentPolicy("Prepaid");

        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        hotel.setHotelName("Test Hotel"); // Ensure hotel name is set

        HotelContract contract = new HotelContract();
        contract.setId(1L);
        contract.setHotel(hotel); // Ensure hotel is set in contract

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(contractRepository.save(any())).thenReturn(contract);

        HotelContractDTO result = contractService.updateContract(1L, contractDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Contract updated successfully", result.getMessage());
    }

    @Test
    void updateContract_NotFound() {
        HotelContractDTO contractDTO = new HotelContractDTO();
        when(contractRepository.findById(1L)).thenReturn(Optional.empty());

        HotelContractDTO result = contractService.updateContract(1L, contractDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Contract not found", result.getMessage());
    }

    @Test
    void deleteContract_Success() {
        doNothing().when(contractRepository).deleteById(1L);

        HotelContractDTO result = contractService.deleteContract(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Contract deleted successfully", result.getMessage());
    }

    @Test
    void deleteContract_Error() {
        doThrow(new RuntimeException("Error")).when(contractRepository).deleteById(1L);

        HotelContractDTO result = contractService.deleteContract(1L);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Error", result.getMessage());
    }

    @Test
    void getContractsByHotelId_Success() {
        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        hotel.setHotelName("Test Hotel");

        HotelContract contract = new HotelContract();
        contract.setHotel(hotel);

        List<HotelContract> contracts = new ArrayList<>();
        contracts.add(contract);
        when(contractRepository.findByHotel_HotelID(1L)).thenReturn(contracts);

        List<HotelContractDTO> result = contractService.getContractsByHotelId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getContractsByHotelId_Empty() {
        when(contractRepository.findByHotel_HotelID(1L)).thenReturn(Collections.emptyList());

        List<HotelContractDTO> result = contractService.getContractsByHotelId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateContractStatus_Success() {
        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        hotel.setHotelName("Test Hotel");

        HotelContract contract = new HotelContract();
        contract.setId(1L);
        contract.setIsActive(true);
        contract.setHotel(hotel); // Ensure hotel is set in contract

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        when(contractRepository.save(any())).thenReturn(contract);

        HotelContractDTO result = contractService.updateContractStatus(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Contract status updated successfully", result.getMessage());
    }

    @Test
    void updateContractStatus_NotFound() {
        when(contractRepository.findById(1L)).thenReturn(Optional.empty());

        HotelContractDTO result = contractService.updateContractStatus(1L);

        assertEquals(404, result.getStatusCode());
        assertEquals("Contract not found", result.getMessage());
    }
}