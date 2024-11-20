package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.BookingDTO;
import com.hotel.horizonstay.entity.Booking;
import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.repository.BookingRepository;
import com.hotel.horizonstay.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void getBookingsByEmail_Success() {
        String email = "test@example.com";
        Booking booking = new Booking();
        booking.setEmail(email);
        booking.setHotelID(1L);
        booking.setBookingId(1L);
        booking.setFullName("John Doe");
        booking.setTelephone("1234567890");
        booking.setAddress("123 Street");
        booking.setCity("City");
        booking.setCountry("Country");
        booking.setCheckIn(LocalDate.now());
        booking.setCheckOut(LocalDate.now().plusDays(1));
        booking.setNoOfAdults(2);
        booking.setNoOfChildren(1);
        booking.setDiscount(10.0F);
        booking.setPayableAmount(100.0F);
        booking.setItems(Collections.emptyList()); // Initialize items

        when(bookingRepository.findByEmail(email)).thenReturn(List.of(booking));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(new Hotel()));

        List<BookingDTO> result = bookingService.getBookingsByEmail(email);

        assertEquals(1, result.size());
        assertEquals(email, result.get(0).getEmail());
    }

    @Test
    void getBookingsByEmail_NoBookings() {
        String email = "test@example.com";
        when(bookingRepository.findByEmail(email)).thenReturn(List.of());

        List<BookingDTO> result = bookingService.getBookingsByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveBooking_Success() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEmail("test@example.com");
        bookingDTO.setHotelID(1L);
        bookingDTO.setFullName("John Doe");
        bookingDTO.setTelephone("1234567890");
        bookingDTO.setAddress("123 Street");
        bookingDTO.setCity("City");
        bookingDTO.setCountry("Country");
        bookingDTO.setCheckIn(LocalDate.now().toString());
        bookingDTO.setCheckOut(LocalDate.now().plusDays(1).toString());
        bookingDTO.setNoOfAdults(2);
        bookingDTO.setNoOfChildren(1);
        bookingDTO.setDiscount(10.0F);
        bookingDTO.setPayableAmount(100.0F);
        bookingDTO.setItems(Collections.emptyList()); // Initialize items field

        Booking booking = new Booking();
        booking.setEmail("test@example.com");
        booking.setHotelID(1L);
        booking.setCheckIn(LocalDate.now()); // Initialize checkIn field
        booking.setCheckOut(LocalDate.now().plusDays(1)); // Initialize checkOut field
        booking.setItems(Collections.emptyList()); // Initialize items field

        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDTO result = bookingService.saveBooking(bookingDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Booking saved successfully", result.getMessage());
    }

    @Test
    void updateBooking_Success() {
        Long bookingId = 1L;
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEmail("test@example.com");
        bookingDTO.setHotelID(1L);
        bookingDTO.setFullName("John Doe");
        bookingDTO.setTelephone("1234567890");
        bookingDTO.setAddress("123 Street");
        bookingDTO.setCity("City");
        bookingDTO.setCountry("Country");
        bookingDTO.setCheckIn(LocalDate.now().toString());
        bookingDTO.setCheckOut(LocalDate.now().plusDays(1).toString());
        bookingDTO.setNoOfAdults(2);
        bookingDTO.setNoOfChildren(1);
        bookingDTO.setDiscount(10.0F);
        bookingDTO.setPayableAmount(100.0F);
        bookingDTO.setItems(Collections.emptyList()); // Initialize items field

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setEmail("test@example.com");
        booking.setHotelID(1L);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDTO result = bookingService.updateBooking(bookingId, bookingDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Booking updated successfully", result.getMessage());
    }

    @Test
    void updateBooking_NotFound() {
        Long bookingId = 1L;
        BookingDTO bookingDTO = new BookingDTO();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        BookingDTO result = bookingService.updateBooking(bookingId, bookingDTO);

        assertEquals(400, result.getStatusCode());
        assertEquals("Booking not found with id: " + bookingId, result.getMessage());
    }

    @Test
    void deleteBooking_Success() {
        Long bookingId = 1L;

        doNothing().when(bookingRepository).deleteById(bookingId);

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void deleteBooking_NotFound() {
        Long bookingId = 1L;

        doThrow(new IllegalArgumentException("Booking not found with id: " + bookingId))
                .when(bookingRepository).deleteById(bookingId);

        assertThrows(IllegalArgumentException.class, () -> bookingService.deleteBooking(bookingId));
    }

    @Test
    void getBookingById_Success() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setEmail("test@example.com");
        booking.setHotelID(1L);
        booking.setCheckIn(LocalDate.now()); // Initialize checkIn field
        booking.setCheckOut(LocalDate.now().plusDays(1)); // Initialize checkOut field
        booking.setItems(Collections.emptyList()); // Initialize items field

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(new Hotel()));

        BookingDTO result = bookingService.getBookingById(bookingId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Booking fetched successfully", result.getMessage());
    }

    @Test
    void getBookingById_NotFound() {
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        BookingDTO result = bookingService.getBookingById(bookingId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Booking not found with id: " + bookingId, result.getMessage());
    }
}