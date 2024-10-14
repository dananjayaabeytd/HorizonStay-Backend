package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.DiscountDTO;
import com.hotel.horizonstay.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class DiscountControllerTest {

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDiscountToSeason_Success()
    {
        DiscountDTO discountDTO = createDiscountDTO();
        when(discountService.addDiscountToSeason(anyLong(), any(DiscountDTO.class))).thenReturn(discountDTO);

        ResponseEntity<DiscountDTO> response = discountController.addDiscountToSeason(1L, discountDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(discountDTO, response.getBody());
    }

    @Test
    void testAddDiscountToSeason_InvalidArgument()
    {
        when(discountService.addDiscountToSeason(anyLong(), any(DiscountDTO.class))).thenThrow(new IllegalArgumentException("Invalid season ID"));

        ResponseEntity<DiscountDTO> response = discountController.addDiscountToSeason(1L, createDiscountDTO());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid season ID", response.getBody().getMessage());
    }

    @Test
    void testAddDiscountToSeason_Exception()
    {
        when(discountService.addDiscountToSeason(anyLong(), any(DiscountDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<DiscountDTO> response = discountController.addDiscountToSeason(1L, createDiscountDTO());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding discount", response.getBody().getMessage());
    }

    @Test
    void testGetDiscountById_Success()
    {
        DiscountDTO discountDTO = createDiscountDTO();
        when(discountService.getDiscountById(anyLong())).thenReturn(discountDTO);

        ResponseEntity<DiscountDTO> response = discountController.getDiscountById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discountDTO, response.getBody());
    }

    @Test
    void testGetDiscountById_InvalidArgument()
    {
        when(discountService.getDiscountById(anyLong())).thenThrow(new IllegalArgumentException("Invalid discount ID"));

        ResponseEntity<DiscountDTO> response = discountController.getDiscountById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid discount ID", response.getBody().getMessage());
    }

    @Test
    void testGetDiscountById_Exception()
    {
        when(discountService.getDiscountById(anyLong())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<DiscountDTO> response = discountController.getDiscountById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching discount", response.getBody().getMessage());
    }

    @Test
    void testUpdateDiscount_Success()
    {
        DiscountDTO discountDTO = createDiscountDTO();
        when(discountService.updateDiscount(anyLong(), any(DiscountDTO.class))).thenReturn(discountDTO);

        ResponseEntity<DiscountDTO> response = discountController.updateDiscount(1L, discountDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discountDTO, response.getBody());
    }

    @Test
    void testUpdateDiscount_InvalidArgument()
    {
        when(discountService.updateDiscount(anyLong(), any(DiscountDTO.class))).thenThrow(new IllegalArgumentException("Invalid discount ID"));

        ResponseEntity<DiscountDTO> response = discountController.updateDiscount(1L, createDiscountDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid discount ID", response.getBody().getMessage());
    }

    @Test
    void testUpdateDiscount_Exception()
    {
        when(discountService.updateDiscount(anyLong(), any(DiscountDTO.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<DiscountDTO> response = discountController.updateDiscount(1L, createDiscountDTO());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating discount", response.getBody().getMessage());
    }

    @Test
    void testDeleteDiscount_Success()
    {
        ResponseEntity<DiscountDTO> response = discountController.deleteDiscount(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteDiscount_InvalidArgument()
    {
        doThrow(new IllegalArgumentException("Invalid discount ID")).when(discountService).deleteDiscount(anyLong());

        ResponseEntity<DiscountDTO> response = discountController.deleteDiscount(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid discount ID", response.getBody().getMessage());
    }

    @Test
    void testDeleteDiscount_Exception()
    {
        doThrow(new RuntimeException("Error")).when(discountService).deleteDiscount(anyLong());

        ResponseEntity<DiscountDTO> response = discountController.deleteDiscount(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting discount", response.getBody().getMessage());
    }

    @Test
    void testGetDiscountsBySeasonId_Success()
    {
        List<DiscountDTO> discounts = Collections.singletonList(createDiscountDTO());
        when(discountService.getDiscountsBySeasonId(anyLong())).thenReturn(discounts);

        ResponseEntity<List<DiscountDTO>> response = discountController.getDiscountsBySeasonId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(discounts, response.getBody());
    }

    @Test
    void testGetDiscountsBySeasonId_Exception()
    {
        when(discountService.getDiscountsBySeasonId(anyLong())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<DiscountDTO>> response = discountController.getDiscountsBySeasonId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching discounts", response.getBody() != null ? response.getBody().get(0).getMessage() : "Error occurred while fetching discounts");
    }

    private DiscountDTO createDiscountDTO()
    {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Summer Sale");
        discountDTO.setPercentage(20.0F);
        return discountDTO;
    }
}