package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.*;
import com.hotel.horizonstay.service.BookingService;
import com.hotel.horizonstay.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class BookingControllerTest {

    @Mock
    private ContractService contractService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateAmount_Success()
    {
        // Create a valid CalculationDTO object
        CalculationDTO bookingRequest = createCalculationDTO();
        // Mock the result of the calculatePayableAmount method
        Map<String, Float> result = new HashMap<>();
        result.put("totalAmount", 1000.0f);
        result.put("discountAmount", 200.0f);

        // Define behavior of contractService.calculatePayableAmount
        when(contractService.calculatePayableAmount(any(CalculationDTO.class))).thenReturn(result);

        // Call the calculateAmount method
        ResponseEntity<Map<String, Object>> response = bookingController.calculateAmount(bookingRequest);

        // Assert the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1000.0f, response.getBody().get("payableAmount"));
        assertEquals(200.0f, response.getBody().get("discountAmount"));
    }

    @Test
    void testCalculateAmount_NullRequestBody()
    {
        // Call the calculateAmount method with null request body
        ResponseEntity<Map<String, Object>> response = bookingController.calculateAmount(null);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", response.getBody().get("error"));
    }

    @Test
    void testCalculateAmount_InvalidDateFormat()
    {
        // Create a CalculationDTO object with invalid date format
        CalculationDTO bookingRequest = createCalculationDTO();
        bookingRequest.setCheckIn("invalid-date");

        // Call the calculateAmount method
        ResponseEntity<Map<String, Object>> response = bookingController.calculateAmount(bookingRequest);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date format. Please use YYYY-MM-DD.", response.getBody().get("error"));
    }

    @Test
    void testCalculateAmount_CheckInAfterCheckOut()
    {
        // Create a CalculationDTO object with check-in date after check-out date
        CalculationDTO bookingRequest = createCalculationDTO();
        bookingRequest.setCheckIn("2025-12-16");

        // Call the calculateAmount method
        ResponseEntity<Map<String, Object>> response = bookingController.calculateAmount(bookingRequest);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Check-in date must be before check-out date.", response.getBody().get("error"));
    }

    @Test
    void testCalculateAmount_Exception()
    {
        // Create a valid CalculationDTO object
        CalculationDTO bookingRequest = createCalculationDTO();

        // Define behavior of contractService.calculatePayableAmount to throw a runtime exception
        when(contractService.calculatePayableAmount(any(CalculationDTO.class))).thenThrow(new RuntimeException("Error"));

        // Call the calculateAmount method
        ResponseEntity<Map<String, Object>> response = bookingController.calculateAmount(bookingRequest);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while calculating the amount", response.getBody().get("error"));
    }

    @Test
    void testSaveBooking_Success()
    {
        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();

        // Define behavior of bookingService.saveBooking
        when(bookingService.saveBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        // Call the saveBooking method
        ResponseEntity<BookingDTO> response = bookingController.saveBooking(bookingDTO);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testSaveBooking_NullRequestBody()
    {
        // Call the saveBooking method with null request body
        ResponseEntity<BookingDTO> response = bookingController.saveBooking(null);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", response.getBody().getMessage());
    }

    @Test
    void testSaveBooking_MissingRequiredFields()
    {
        // Create a BookingDTO object with missing required fields
        BookingDTO bookingDTO = new BookingDTO();

        // Call the saveBooking method
        ResponseEntity<BookingDTO> response = bookingController.saveBooking(bookingDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Missing required fields: fullName, email, checkIn, or checkOut", response.getBody().getMessage());
    }

    @Test
    void testSaveBooking_Exception()
    {
        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();

        // Define behavior of bookingService.saveBooking to throw a runtime exception
        when(bookingService.saveBooking(any(BookingDTO.class))).thenThrow(new RuntimeException("Error"));

        // Call the saveBooking method
        ResponseEntity<BookingDTO> response = bookingController.saveBooking(bookingDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while saving the booking", response.getBody().getMessage());
    }

    @Test
    void testGetBookingsByEmail_Success()
    {
        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();
        List<BookingDTO> bookingDTOs = Collections.singletonList(bookingDTO);

        // Define behavior of bookingService.getBookingsByEmail
        when(bookingService.getBookingsByEmail(anyString())).thenReturn(bookingDTOs);

        // Call the getBookingsByEmail method
        ResponseEntity<List<BookingDTO>> response = bookingController.getBookingsByEmail("tharindu@gmail.com");

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Tharindu Dananjaya", response.getBody().get(0).getFullName());
    }

    @Test
    void testGetBookingsByEmail_NotFound()
    {
        // Define behavior of bookingService.getBookingsByEmail to return an empty list
        when(bookingService.getBookingsByEmail(anyString())).thenReturn(Collections.emptyList());

        // Call the getBookingsByEmail method
        ResponseEntity<List<BookingDTO>> response = bookingController.getBookingsByEmail("tharindu@gmail.com");

        // Assert the response status
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetBookingsByUserId_Success()
    {
        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();
        List<BookingDTO> bookingDTOs = Collections.singletonList(bookingDTO);

        // Define behavior of bookingService.getBookingsByUserId
        when(bookingService.getBookingsByUserId(anyInt())).thenReturn(bookingDTOs);

        // Call the getBookingsByUserId method
        ResponseEntity<List<BookingDTO>> response = bookingController.getBookingsByUserId(1);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Tharindu Dananjaya", response.getBody().get(0).getFullName());
    }

    @Test
    void testGetBookingsByUserId_NotFound()
    {
        // Define behavior of bookingService.getBookingsByUserId to return an empty list
        when(bookingService.getBookingsByUserId(anyInt())).thenReturn(Collections.emptyList());

        // Call the getBookingsByUserId method
        ResponseEntity<List<BookingDTO>> response = bookingController.getBookingsByUserId(1);

        // Assert the response status
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateBooking_Success()
    {
        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();
        // Mock the behavior of bookingService.updateBooking to return the bookingDTO
        when(bookingService.updateBooking(anyLong(), any(BookingDTO.class))).thenReturn(bookingDTO);

        // Call the updateBooking method
        ResponseEntity<BookingDTO> response = bookingController.updateBooking(1L, bookingDTO);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testUpdateBooking_NullRequestBody()
    {
        // Call the updateBooking method with null request body
        ResponseEntity<BookingDTO> response = bookingController.updateBooking(1L, null);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request body is null", response.getBody().getMessage());
    }

    @Test
    void testUpdateBooking_InvalidArgument()
    {
        // Mock the behavior of bookingService.updateBooking to throw IllegalArgumentException
        when(bookingService.updateBooking(anyLong(), any(BookingDTO.class))).thenThrow(new IllegalArgumentException("Invalid ID"));

        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();
        // Call the updateBooking method
        ResponseEntity<BookingDTO> response = bookingController.updateBooking(1L, bookingDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody().getMessage());
    }

    @Test
    void testUpdateBooking_Exception()
    {
        // Mock the behavior of bookingService.updateBooking to throw RuntimeException
        when(bookingService.updateBooking(anyLong(), any(BookingDTO.class))).thenThrow(new RuntimeException("Error"));

        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();
        // Call the updateBooking method
        ResponseEntity<BookingDTO> response = bookingController.updateBooking(1L, bookingDTO);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating the booking", response.getBody().getMessage());
    }

    @Test
    void testDeleteBooking_Success()
    {
        // Call the deleteBooking method
        ResponseEntity<BookingDTO> response = bookingController.deleteBooking(1L);

        // Assert the response status
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteBooking_InvalidArgument()
    {
        // Mock the behavior of bookingService.deleteBooking to throw IllegalArgumentException
        doThrow(new IllegalArgumentException("Invalid ID")).when(bookingService).deleteBooking(anyLong());

        // Call the deleteBooking method
        ResponseEntity<BookingDTO> response = bookingController.deleteBooking(1L);

        // Assert the response status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteBooking_Exception()
    {
        // Mock the behavior of bookingService.deleteBooking to throw RuntimeException
        doThrow(new RuntimeException("Error")).when(bookingService).deleteBooking(anyLong());

        // Call the deleteBooking method
        ResponseEntity<BookingDTO> response = bookingController.deleteBooking(1L);

        // Assert the response status
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetBookingById_Success()
    {
        // Create a valid BookingDTO object
        BookingDTO bookingDTO = createBookingDTO();
        // Mock the behavior of bookingService.getBookingById to return the bookingDTO
        when(bookingService.getBookingById(anyLong())).thenReturn(bookingDTO);

        // Call the getBookingById method
        ResponseEntity<BookingDTO> response = bookingController.getBookingById(1L);

        // Assert the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testGetBookingById_InvalidArgument()
    {
        // Mock the behavior of bookingService.getBookingById to throw IllegalArgumentException
        when(bookingService.getBookingById(anyLong())).thenThrow(new IllegalArgumentException("Invalid ID"));

        // Call the getBookingById method
        ResponseEntity<BookingDTO> response = bookingController.getBookingById(1L);

        // Assert the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody().getMessage());
    }

    @Test
    void testGetBookingById_Exception()
    {
        // Mock the behavior of bookingService.getBookingById to throw RuntimeException
        when(bookingService.getBookingById(anyLong())).thenThrow(new RuntimeException("Error"));

        // Call the getBookingById method
        ResponseEntity<BookingDTO> response = bookingController.getBookingById(1L);

        // Assert the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching the booking", response.getBody().getMessage());
    }

    // Helper method to create a valid CalculationDTO object
    private CalculationDTO createCalculationDTO()
    {
        CalculationDTO calculationDTO = new CalculationDTO();
        calculationDTO.setCheckIn("2025-11-11");
        calculationDTO.setCheckOut("2025-12-15");
        calculationDTO.setNoOfAdults(2);
        calculationDTO.setNoOfChildren(1);
        calculationDTO.setDiscountPercentage(20.0f);
        calculationDTO.setMarkupPercentage(10.0f);

        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Spa Access");
        supplementDTO.setPrice(50.0f);
        supplementDTO.setQuantity(2);

        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("King Suite");
        roomTypeDTO.setPrice(300.0f);
        roomTypeDTO.setNumberOfRooms(4);

        calculationDTO.setSupplements(Collections.singletonList(supplementDTO));
//        calculationDTO.setRoomType(Collections.singletonList(roomTypeDTO));

        return calculationDTO;
    }

    // Helper method to create a valid BookingDTO object
    private BookingDTO createBookingDTO()
    {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setFullName("Tharindu Dananjaya");
        bookingDTO.setTelephone("0123456789");
        bookingDTO.setEmail("tharindu@gmail.com");
        bookingDTO.setAddress("123 Beach Road");
        bookingDTO.setCity("Colombo");
        bookingDTO.setCountry("Sri Lanka");
        bookingDTO.setCheckIn("2024-09-15");
        bookingDTO.setCheckOut("2024-09-20");
        bookingDTO.setNoOfAdults(2);
        bookingDTO.setNoOfChildren(1);
        bookingDTO.setDiscount(10.0f);
        bookingDTO.setPayableAmount(500.0f);

        BookingItemDTO item1 = new BookingItemDTO();
        item1.setName("Deluxe Room");
        item1.setPrice(100.0f);
        item1.setQuantity(2);
        item1.setTotalAmount(200.0f);

        BookingItemDTO item2 = new BookingItemDTO();
        item2.setName("Breakfast Supplement");
        item2.setPrice(20.0f);
        item2.setQuantity(3);
        item2.setTotalAmount(60.0f);

        BookingItemDTO item3 = new BookingItemDTO();
        item3.setName("Airport Transfer");
        item3.setPrice(50.0f);
        item3.setQuantity(1);
        item3.setTotalAmount(50.0f);

        bookingDTO.setItems(List.of(item1, item2, item3));

        return bookingDTO;
    }
}