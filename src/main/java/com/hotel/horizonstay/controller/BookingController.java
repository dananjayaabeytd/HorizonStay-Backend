//package com.hotel.horizonstay.controller;
//
//import com.hotel.horizonstay.dto.BookingDTO;
//import com.hotel.horizonstay.dto.CalculationDTO;
//import com.hotel.horizonstay.helper.ErrorResponse;
//import com.hotel.horizonstay.service.BookingService;
//import com.hotel.horizonstay.service.HotelContractService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/booking")
//public class BookingController {
//
//    @Autowired
//    private HotelContractService hotelContractService;
//
//    @Autowired
//    private BookingService bookingService;
//
//    private final ErrorResponse error = new ErrorResponse();
//
//    // Endpoint to calculate the payable amount for a booking
//    @PostMapping("/calculate-amount")
//    public ResponseEntity<Map<String, Object>> calculateAmount(@RequestBody CalculationDTO bookingRequest)
//    {
//
//        if (bookingRequest == null)
//        {
//            return error.createErrorResponseMap("Request body is null", HttpStatus.BAD_REQUEST);
//        }
//
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        try
//        {
//            LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckIn(), dateFormatter);
//            LocalDate checkOutDate = LocalDate.parse(bookingRequest.getCheckOut(), dateFormatter);
//
//            if (!checkInDate.isBefore(checkOutDate))
//            {
//                return error.createErrorResponseMap("Check-in date must be before check-out date.", HttpStatus.BAD_REQUEST);
//            }
//
//            Map<String, Float> result = hotelContractService.calculatePayableAmount(bookingRequest);
//            Map<String, Object> response = new HashMap<>();
//            response.put("payableAmount", result.get("totalAmount"));
//            response.put("discountAmount", result.get("discountAmount"));
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        }
//        catch (DateTimeParseException e)
//        {
//            return error.createErrorResponseMap("Invalid date format. Please use YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
//        }
//        catch (Exception e)
//        {
//            return error.createErrorResponseMap("Error occurred while calculating the amount", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Endpoint to save a booking
//    @PostMapping("/add")
//    public ResponseEntity<BookingDTO> saveBooking(@RequestBody BookingDTO bookingDTO)
//    {
//
//        // Check if the request body is null
//        if (bookingDTO == null)
//        {
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage("Request body is null");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//        }
//
//        try
//        {
//            // Validate required fields
//            if (bookingDTO.getFullName() == null || bookingDTO.getFullName().isEmpty() || bookingDTO.getEmail() == null || bookingDTO.getEmail().isEmpty() || bookingDTO.getCheckIn() == null || bookingDTO.getCheckOut() == null)
//            {
//                BookingDTO errorResponse = new BookingDTO();
//                errorResponse.setMessage("Missing required fields: fullName, email, checkIn, or checkOut");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//            }
//
//            // Save the booking using the booking service
//            BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);
//            return ResponseEntity.ok(savedBooking);
//        }
//        catch (IllegalArgumentException e)
//        {
//            // Handle invalid arguments
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//
//        }
//        catch (Exception e)
//        {
//            // Handle general exceptions
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage("Error occurred while saving the booking");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//
//    // Endpoint to get bookings by email
//    @GetMapping("/{email}")
//    public ResponseEntity<List<BookingDTO>> getBookingsByEmail(@PathVariable String email)
//    {
//        try
//        {
//            // Retrieve bookings by email using the booking service
//            List<BookingDTO> bookingDTOs = bookingService.getBookingsByEmail(email);
//
//            // Check if the list of bookings is empty
//            if (bookingDTOs.isEmpty())
//            {
//                BookingDTO errorResponse = new BookingDTO();
//                errorResponse.setMessage("no bookings found related to that email");
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonList(errorResponse));
//            }
//
//            return ResponseEntity.ok(bookingDTOs);
//        }
//        catch (Exception e)
//        {
//            // Handle general exceptions
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage("Error occurred while fetch bookings");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorResponse));
//        }
//    }
//
//    @GetMapping("user/{userId}")
//    public ResponseEntity<List<BookingDTO>> getBookingsByUserId(@PathVariable Integer userId)
//    {
//        try
//        {
//            // Retrieve bookings by email using the booking service
//            List<BookingDTO> bookingDTOs = bookingService.getBookingsByUserId(userId);
//
//            // Check if the list of bookings is empty
//            if (bookingDTOs.isEmpty())
//            {
//                BookingDTO errorResponse = new BookingDTO();
//                errorResponse.setMessage("no bookings found related to that email");
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonList(errorResponse));
//            }
//
//            return ResponseEntity.ok(bookingDTOs);
//        }
//        catch (Exception e)
//        {
//            // Handle general exceptions
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage("Error occurred while fetch bookings");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList(errorResponse));
//        }
//    }
//
//
//    // Endpoint to update a booking
//    @PutMapping("/admin/update/{id}")
//    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO)
//    {
//        if (bookingDTO == null)
//        {
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage("Request body is null");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//        }
//
//        try
//        {
//            BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
//            return ResponseEntity.ok(updatedBooking);
//        }
//        catch (IllegalArgumentException e)
//        {
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//        }
//        catch (Exception e)
//        {
//            BookingDTO errorResponse = new BookingDTO();
//            errorResponse.setMessage("Error occurred while updating the booking");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//
//    // Endpoint to delete a booking
//    @DeleteMapping("/admin/delete/{id}")
//    public ResponseEntity<BookingDTO> deleteBooking(@PathVariable Long id)
//    {
//        try
//        {
//            bookingService.deleteBooking(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//        catch (IllegalArgumentException e)
//        {
//            return error.createErrorResponseBooking(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//        catch (Exception e)
//        {
//            return error.createErrorResponseBooking("Error occurred while deleting the booking", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Endpoint to get a booking by ID
//    @GetMapping("/admin/get/{id}")
//    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id)
//    {
//        try
//        {
//            BookingDTO bookingDTO = bookingService.getBookingById(id);
//            return ResponseEntity.ok(bookingDTO);
//        }
//        catch (IllegalArgumentException e)
//        {
//            return error.createErrorResponseBooking(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//        catch (Exception e)
//        {
//            return error.createErrorResponseBooking("Error occurred while fetching the booking", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//}