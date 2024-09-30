package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    @BeforeEach
    void setUp()
    {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetAllHotels_Success()
//    {
//        // Arrange: Create sample HotelDTO objects
//        HotelDTO hotel1 = new HotelDTO();
//        hotel1.setHotelName("Hotel 1");
//        HotelDTO hotel2 = new HotelDTO();
//        hotel2.setHotelName("Hotel 2");
//
//        // Mock the getAllHotels method of hotelService to return the sample hotels
//        List<HotelDTO> hotels = Arrays.asList(hotel1, hotel2);
//        when(hotelService.getAllHotels()).thenReturn(hotels);
//
//        // Act: Call the getAllHotels method of hotelController
//        ResponseEntity<List<HotelDTO>> response = hotelController.getAllHotels();
//
//        // Assert: Verify the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
//    }

//    @Test
//    void testGetAllHotels_NoHotelsFound()
//    {
//        // Mock the getAllHotels method of hotelService to return an empty list
//        when(hotelService.getAllHotels()).thenReturn(List.of());
//
//        // Act: Call the getAllHotels method of hotelController
//        ResponseEntity<List<HotelDTO>> response = hotelController.getAllHotels();
//
//        // Assert: Verify the response status and body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(0, Objects.requireNonNull(response.getBody()).size());
//    }

//    @Test
//    void testGetAllHotels_Exception()
//    {
//        // Mock the getAllHotels method of hotelService to throw an exception
//        when(hotelService.getAllHotels()).thenThrow(new RuntimeException("Error Getting all hotels"));
//
//        // Act: Call the getAllHotels method of hotelController
//        ResponseEntity<List<HotelDTO>> response = hotelController.getAllHotels();
//
//        // Assert: Verify the response status and error message
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Error occurred while fetching hotels", Objects.requireNonNull(response.getBody()).get(0).getMessage());
//    }

    @Test
    void testGetHotelById_Success()
    {
        // Arrange: Create a sample HotelDTO object
        HotelDTO hotel = new HotelDTO();
        hotel.setHotelName("Hotel 1");

        // Mock the getHotelById method of hotelService to return the sample hotel
        when(hotelService.getHotelById(anyLong())).thenReturn(hotel);

        // Act: Call the getHotelById method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.getHotelById(1L);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel 1", Objects.requireNonNull(response.getBody()).getHotelName());
    }

    @Test
    void testGetHotelById_NotFound()
    {
        // Mock the getHotelById method of hotelService to return null
        when(hotelService.getHotelById(anyLong())).thenReturn(null);

        // Act: Call the getHotelById method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.getHotelById(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Hotel not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetHotelById_Exception()
    {
        // Mock the getHotelById method of hotelService to throw an exception
        when(hotelService.getHotelById(anyLong())).thenThrow(new RuntimeException("Error Getting hotel by ID"));

        // Act: Call the getHotelById method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.getHotelById(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching hotel", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testAddHotel_Success()
    {
        // Arrange: Create a sample HotelDTO object
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("New Hotel");
        hotelDTO.setHotelEmail("hotel@gmail.com");
        hotelDTO.setHotelDescription("great");
        hotelDTO.setHotelContactNumber("123-456-7890");
        hotelDTO.setHotelCity("colombo");
        hotelDTO.setHotelCountry("usa");
        hotelDTO.setHotelRating(4.0f);
        hotelDTO.setHotelImages(Arrays.asList("image1.jpg", "image2.jpg"));

        // Mock the addHotel method of hotelService to return the sample hotel
        when(hotelService.addHotel(any(HotelDTO.class))).thenReturn(hotelDTO);

        // Act: Call the addHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.addHotel(hotelDTO);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Hotel", Objects.requireNonNull(response.getBody()).getHotelName());
    }

    @Test
    void testAddHotel_NullRequest()
    {
        // Act: Call the addHotel method with null request body
        ResponseEntity<HotelDTO> response = hotelController.addHotel(null);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testAddHotel_InvalidData()
    {
        // Arrange: Create a HotelDTO object with invalid data
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("");

        // Act: Call the addHotel method with invalid data
        ResponseEntity<HotelDTO> response = hotelController.addHotel(hotelDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid hotel data", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testAddHotel_Exception()
    {
        // Arrange: Create a sample HotelDTO object
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("New Hotel");
        hotelDTO.setHotelDescription("superb");
        hotelDTO.setHotelContactNumber("123-456-7890");
        hotelDTO.setHotelEmail("test@gmail.com");
        hotelDTO.setHotelCity("colombo");
        hotelDTO.setHotelCountry("usa");
        hotelDTO.setHotelRating(4.0f);
        hotelDTO.setHotelImages(Arrays.asList("image1.jpg", "image2.jpg"));

        // Mock the addHotel method of hotelService to throw an exception
        when(hotelService.addHotel(any(HotelDTO.class))).thenThrow(new RuntimeException("Error Adding hotel"));

        // Act: Call the addHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.addHotel(hotelDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding hotel", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateHotel_Success()
    {
        // Arrange: Create a sample HotelDTO object
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Updated Hotel");
        hotelDTO.setHotelDescription("superb updated");
        hotelDTO.setHotelContactNumber("123-456-7890");
        hotelDTO.setHotelEmail("test@gmail.com");
        hotelDTO.setHotelCity("colombo");
        hotelDTO.setHotelCountry("usa");
        hotelDTO.setHotelRating(5.0f);
        hotelDTO.setHotelImages(Arrays.asList("image1.jpg", "image2.jpg"));

        // Mock the updateHotel method of hotelService to return the sample hotel
        when(hotelService.updateHotel(anyLong(), any(HotelDTO.class))).thenReturn(hotelDTO);

        // Act: Call the updateHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.updateHotel(1L, hotelDTO);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Hotel", Objects.requireNonNull(response.getBody()).getHotelName());
    }

    @Test
    void testUpdateHotel_NotFound()
    {
        // Arrange: Create a sample HotelDTO object
        HotelDTO request = new HotelDTO();
        request.setHotelName("Updated Hotel");
        request.setHotelDescription("superb updated");
        request.setHotelContactNumber("123-456-7890");
        request.setHotelCity("colombo");
        request.setHotelCountry("usa");
        request.setHotelRating(5.0f);
        request.setHotelImages(Arrays.asList("image1.jpg", "image2.jpg"));

        // Mock the updateHotel method of hotelService to return null
        when(hotelService.updateHotel(anyLong(), any(HotelDTO.class))).thenReturn(null);

        // Act: Call the updateHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.updateHotel(1L, request);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Hotel not found for update", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateHotel_InvalidData()
    {
        // Arrange: Create a HotelDTO object with invalid data
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("");

        // Act: Call the updateHotel method with invalid data
        ResponseEntity<HotelDTO> response = hotelController.updateHotel(1L, hotelDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid hotel data", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateHotel_Exception()
    {
        // Arrange: Create a sample HotelDTO object
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Updated Hotel");
        hotelDTO.setHotelDescription("superb updated");
        hotelDTO.setHotelContactNumber("123-456-7890");
        hotelDTO.setHotelEmail("test@gmail.com");
        hotelDTO.setHotelCity("colombo");
        hotelDTO.setHotelCountry("usa");
        hotelDTO.setHotelRating(5.0f);
        hotelDTO.setHotelImages(Arrays.asList("image1.jpg", "image2.jpg"));

        // Mock the updateHotel method of hotelService to throw an exception
        when(hotelService.updateHotel(anyLong(), any(HotelDTO.class))).thenThrow(new RuntimeException("Error Updating hotel"));

        // Act: Call the updateHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.updateHotel(1L, hotelDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating hotel", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteHotel_Success()
    {
        // Act: Call the deleteHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.deleteHotel(1L);

        // Assert: Verify the response status
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteHotel_Exception()
    {
        // Mock the deleteHotel method of hotelService to throw an exception
        doThrow(new RuntimeException("Error Deleting hotel")).when(hotelService).deleteHotel(anyLong());

        // Act: Call the deleteHotel method of hotelController
        ResponseEntity<HotelDTO> response = hotelController.deleteHotel(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting hotel", Objects.requireNonNull(response.getBody()).getMessage());
    }
}