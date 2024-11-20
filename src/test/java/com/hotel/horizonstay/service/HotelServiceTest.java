package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllHotels_Success() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);

        HotelDTO result = hotelService.getAllHotels();

        assertEquals(200, result.getStatusCode());
        assertEquals("Hotels retrieved successfully", result.getMessage());
    }

    @Test
    void getAllHotels_NoHotelsFound() {
        when(hotelRepository.findAll()).thenReturn(new ArrayList<>());

        HotelDTO result = hotelService.getAllHotels();

        assertEquals(404, result.getStatusCode());
        assertEquals("No hotels found", result.getMessage());
    }

    @Test
    void getAllHotels_Error() {
        when(hotelRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        HotelDTO result = hotelService.getAllHotels();

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getHotelById_Success() {
        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelDTO result = hotelService.getHotelById(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Hotel found successfully", result.getMessage());
    }

    @Test
    void getHotelById_NotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        HotelDTO result = hotelService.getHotelById(1L);

        assertEquals(404, result.getStatusCode());
        assertEquals("Hotel not found", result.getMessage());
    }

    @Test
    void addHotel_Success() {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Test Hotel");
        when(hotelRepository.findByHotelName(any())).thenReturn(Optional.empty());
        when(hotelRepository.findByHotelEmail(any())).thenReturn(Optional.empty());
        when(hotelRepository.save(any())).thenReturn(new Hotel());

        HotelDTO result = hotelService.addHotel(hotelDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Hotel added successfully", result.getMessage());
    }

    @Test
    void addHotel_Conflict() {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Test Hotel");
        when(hotelRepository.findByHotelName(any())).thenReturn(Optional.of(new Hotel()));

        HotelDTO result = hotelService.addHotel(hotelDTO);

        assertEquals(409, result.getStatusCode());
        assertEquals("Hotel already exists with same name or email", result.getMessage());
    }

    @Test
    void updateHotel_Success() {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Updated Hotel");
        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenReturn(hotel);

        HotelDTO result = hotelService.updateHotel(1L, hotelDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Hotel updated successfully", result.getMessage());
    }

    @Test
    void updateHotel_NotFound() {
        HotelDTO hotelDTO = new HotelDTO();
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        HotelDTO result = hotelService.updateHotel(1L, hotelDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Hotel not found for update", result.getMessage());
    }

    @Test
    void deleteHotel_Success() {
        Hotel hotel = new Hotel();
        hotel.setHotelID(1L);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hotelRepository).deleteById(1L);

        HotelDTO result = hotelService.deleteHotel(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Hotel deleted successfully", result.getMessage());
    }

    @Test
    void deleteHotel_NotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        HotelDTO result = hotelService.deleteHotel(1L);

        assertEquals(404, result.getStatusCode());
        assertEquals("Hotel not found for deletion", result.getMessage());
    }

}