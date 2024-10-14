package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.SupplementDTO;
import com.hotel.horizonstay.service.SupplementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class SupplementControllerTest {

    @Mock
    private SupplementService supplementService;

    @InjectMocks
    private SupplementController supplementController;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSupplementToSeason_Success()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Transport");
        supplementDTO.setPrice(1500.0F);
        when(supplementService.addSupplementToSeason(anyLong(), any(SupplementDTO.class))).thenReturn(supplementDTO);

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.addSupplementToSeason(1L, supplementDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Transport", Objects.requireNonNull(response.getBody()).getSupplementName());
    }

    @Test
    void testAddSupplementToSeason_InvalidData()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.addSupplementToSeason(1L, supplementDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAddSupplementToSeason_Exception()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Transport");
        supplementDTO.setPrice(1500.0F);
        when(supplementService.addSupplementToSeason(anyLong(), any(SupplementDTO.class))).thenThrow(new RuntimeException("Error Adding supplement"));

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.addSupplementToSeason(1L, supplementDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding supplement", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetSupplementById_Success()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Transport");
        supplementDTO.setPrice(1500.0F);
        when(supplementService.getSupplementById(anyLong())).thenReturn(supplementDTO);

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.getSupplementById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transport", Objects.requireNonNull(response.getBody()).getSupplementName());
    }

    @Test
    void testGetSupplementById_NotFound()
    {
        // Arrange
        when(supplementService.getSupplementById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.getSupplementById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Supplement not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetSupplementById_Exception()
    {
        // Arrange
        when(supplementService.getSupplementById(anyLong())).thenThrow(new RuntimeException("Error Getting supplement by ID"));

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.getSupplementById(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching supplement", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateSupplement_Success()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Updated Supplement");
        supplementDTO.setPrice(2000.0F);
        when(supplementService.updateSupplement(anyLong(), any(SupplementDTO.class))).thenReturn(supplementDTO);

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.updateSupplement(1L, supplementDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Supplement", Objects.requireNonNull(response.getBody()).getSupplementName());
    }

    @Test
    void testUpdateSupplement_NotFound()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Updated Supplement");
        supplementDTO.setPrice(2000.0F);
        when(supplementService.updateSupplement(anyLong(), any(SupplementDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.updateSupplement(1L, supplementDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Supplement not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateSupplement_Exception()
    {
        // Arrange
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Updated Supplement");
        supplementDTO.setPrice(2000.0F);
        when(supplementService.updateSupplement(anyLong(), any(SupplementDTO.class))).thenThrow(new RuntimeException("Error Updating supplement"));

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.updateSupplement(1L, supplementDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating supplement", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteSupplement_Success()
    {
        // Act
        ResponseEntity<SupplementDTO> response = supplementController.deleteSupplement(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteSupplement_NotFound()
    {
        // Arrange
        doThrow(new IllegalArgumentException("Supplement not found")).when(supplementService).deleteSupplement(anyLong());

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.deleteSupplement(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Supplement not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteSupplement_Exception()
    {
        // Arrange
        doThrow(new RuntimeException("Error Deleting supplement")).when(supplementService).deleteSupplement(anyLong());

        // Act
        ResponseEntity<SupplementDTO> response = supplementController.deleteSupplement(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting supplement", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetSupplementsBySeasonId_Success()
    {
        // Arrange
        SupplementDTO supplement1 = new SupplementDTO();
        supplement1.setSupplementName("Transport");
        supplement1.setPrice(1500.0F);
        SupplementDTO supplement2 = new SupplementDTO();
        supplement2.setSupplementName("Food");
        supplement2.setPrice(1000.0F);

        List<SupplementDTO> supplements = Arrays.asList(supplement1, supplement2);
        when(supplementService.getSupplementsBySeasonId(anyLong())).thenReturn(supplements);

        // Act
        ResponseEntity<List<SupplementDTO>> response = supplementController.getSupplementsBySeasonId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetSupplementsBySeasonId_Exception()
    {
        // Arrange
        when(supplementService.getSupplementsBySeasonId(anyLong())).thenThrow(new RuntimeException("Error Getting supplements by season ID"));

        // Act
        ResponseEntity<List<SupplementDTO>> response = supplementController.getSupplementsBySeasonId(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}