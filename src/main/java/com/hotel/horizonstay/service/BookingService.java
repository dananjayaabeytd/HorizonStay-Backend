package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.BookingDTO;
import com.hotel.horizonstay.dto.BookingItemDTO;
import com.hotel.horizonstay.dto.RoomAvailabilityDTO;
import com.hotel.horizonstay.entity.*;
import com.hotel.horizonstay.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;


    public List<BookingDTO> getBookingsByEmail(String email)
    {
        logger.info("Fetching bookings for email: {}", email);
        List<BookingDTO> bookingDTOs = new ArrayList<>();

        try
        {
            List<Booking> bookings = bookingRepository.findByEmail(email);

            if (!bookings.isEmpty())
            {
                bookingDTOs = bookings.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
            else
            {
                logger.warn("No bookings found for email: {}", email);
            }

        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching bookings for email: {}", email, e);
        }

        return bookingDTOs;
    }

    private BookingDTO mapToDTO(Booking booking)
    {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setHotelID(booking.getHotelID());
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setFullName(booking.getFullName());
        bookingDTO.setTelephone(booking.getTelephone());
        bookingDTO.setEmail(booking.getEmail());
        bookingDTO.setAddress(booking.getAddress());
        bookingDTO.setCity(booking.getCity());
        bookingDTO.setCountry(booking.getCountry());
        bookingDTO.setCheckIn(booking.getCheckIn().toString());
        bookingDTO.setCheckOut(booking.getCheckOut().toString());
        bookingDTO.setNoOfAdults(booking.getNoOfAdults());
        bookingDTO.setNoOfChildren(booking.getNoOfChildren());
        bookingDTO.setDiscount(booking.getDiscount());
        bookingDTO.setPayableAmount(booking.getPayableAmount());

        Hotel hotel = hotelRepository.findById(booking.getHotelID()).orElse(null);

        if (hotel != null)
        {
            bookingDTO.setHotelName(hotel.getHotelName());
            bookingDTO.setHotelLocation(hotel.getHotelCity() + ", " + hotel.getHotelCountry());
            bookingDTO.setHotelDescription(hotel.getHotelDescription());
            bookingDTO.setHotelContactNumber(hotel.getHotelContactNumber());
            bookingDTO.setHotelEmail(hotel.getHotelEmail());
            bookingDTO.setHotelRating(hotel.getHotelRating());
            bookingDTO.setHotelImages(hotel.getHotelImages());
        }

        List<BookingItemDTO> items = booking.getItems().stream().map(item -> {
            BookingItemDTO itemDTO = new BookingItemDTO();
            itemDTO.setName(item.getName());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setTotalAmount(item.getTotalAmount());
            return itemDTO;
        }).collect(Collectors.toList());

        bookingDTO.setItems(items);

        return bookingDTO;
    }

    public BookingDTO saveBooking(BookingDTO bookingDTO)
    {
        logger.info("Saving booking for email: {}", bookingDTO.getEmail());
        BookingDTO res = new BookingDTO();

        try
        {
            Booking booking = new Booking();
            booking.setFullName(bookingDTO.getFullName());
            booking.setTelephone(bookingDTO.getTelephone());
            booking.setEmail(bookingDTO.getEmail());
            booking.setHotelID(bookingDTO.getHotelID());
            booking.setAddress(bookingDTO.getAddress());
            booking.setCity(bookingDTO.getCity());
            booking.setCountry(bookingDTO.getCountry());
            booking.setCheckIn(LocalDate.parse(bookingDTO.getCheckIn()));
            booking.setCheckOut(LocalDate.parse(bookingDTO.getCheckOut()));
            booking.setNoOfAdults(bookingDTO.getNoOfAdults());
            booking.setNoOfChildren(bookingDTO.getNoOfChildren());
            booking.setDiscount(bookingDTO.getDiscount());
            booking.setPayableAmount(bookingDTO.getPayableAmount());

            if (bookingDTO.getSystemUserId() != null)
            {
                SystemUser systemUser = userRepository.findById(bookingDTO.getSystemUserId()).orElse(null);
                booking.setSystemUser(systemUser);
            }

            List<BookingItem> bookingItems = new ArrayList<>();

            for (BookingItemDTO itemDTO : bookingDTO.getItems())
            {
                BookingItem item = new BookingItem();
                item.setName(itemDTO.getName());
                item.setPrice(itemDTO.getPrice());
                item.setQuantity(itemDTO.getQuantity());
                item.setTotalAmount(itemDTO.getTotalAmount());
                item.setBooking(booking);
                bookingItems.add(item);
            }

            booking.setItems(bookingItems);

            Booking savedBooking = bookingRepository.save(booking);
            BookingDTO savedBookingDTO = mapToDTO(savedBooking);
            savedBookingDTO.setStatusCode(200);
            savedBookingDTO.setMessage("Booking saved successfully");

            return savedBookingDTO;

        }
        catch (Exception e)
        {
            logger.error("Error occurred while saving booking for email: {}", bookingDTO.getEmail(), e);
            res.setStatusCode(500);
            res.setMessage("Error occurred while saving booking: " + e.getMessage());
        }

        return res;
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO)
    {
        logger.info("Updating booking with ID: {}", id);
        BookingDTO res = new BookingDTO();

        try
        {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

            booking.setFullName(bookingDTO.getFullName());
            booking.setTelephone(bookingDTO.getTelephone());
            booking.setEmail(bookingDTO.getEmail());
            booking.setHotelID(bookingDTO.getHotelID());
            booking.setAddress(bookingDTO.getAddress());
            booking.setCity(bookingDTO.getCity());
            booking.setCountry(bookingDTO.getCountry());
            booking.setCheckIn(LocalDate.parse(bookingDTO.getCheckIn()));
            booking.setCheckOut(LocalDate.parse(bookingDTO.getCheckOut()));
            booking.setNoOfAdults(bookingDTO.getNoOfAdults());
            booking.setNoOfChildren(bookingDTO.getNoOfChildren());
            booking.setDiscount(bookingDTO.getDiscount());
            booking.setPayableAmount(bookingDTO.getPayableAmount());

            if (bookingDTO.getSystemUserId() != null)
            {
                SystemUser systemUser = userRepository.findById(bookingDTO.getSystemUserId()).orElse(null);
                booking.setSystemUser(systemUser);
            }

            List<BookingItem> bookingItems = new ArrayList<>();

            for (BookingItemDTO itemDTO : bookingDTO.getItems())
            {
                BookingItem item = new BookingItem();
                item.setName(itemDTO.getName());
                item.setPrice(itemDTO.getPrice());
                item.setQuantity(itemDTO.getQuantity());
                item.setTotalAmount(itemDTO.getTotalAmount());
                item.setBooking(booking);
                bookingItems.add(item);
            }

            booking.setItems(bookingItems);

            Booking updatedBooking = bookingRepository.save(booking);
            BookingDTO updatedBookingDTO = mapToDTO(updatedBooking);
            updatedBookingDTO.setStatusCode(200);
            updatedBookingDTO.setMessage("Booking updated successfully");

            return updatedBookingDTO;

        }
        catch (IllegalArgumentException e)
        {
            logger.error("Error occurred while updating booking with ID: {}", id, e);
            res.setStatusCode(400);
            res.setMessage(e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Error occurred while updating booking with ID: {}", id, e);
            res.setStatusCode(500);
            res.setMessage("Error occurred while updating booking: " + e.getMessage());
        }

        return res;
    }

    public void deleteBooking(Long id)
    {
        logger.info("Deleting booking with ID: {}", id);

        try
        {
            bookingRepository.deleteById(id);
            logger.info("Booking with ID: {} deleted successfully", id);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("Error occurred while deleting booking with ID: {}", id, e);
            throw new IllegalArgumentException("Booking not found with id: " + id);
        }
        catch (Exception e)
        {
            logger.error("Error occurred while deleting booking with ID: {}", id, e);
            throw new RuntimeException("Error occurred while deleting booking: " + e.getMessage());
        }
    }

    public BookingDTO getBookingById(Long id)
    {
        logger.info("Fetching booking with ID: {}", id);
        BookingDTO bookingDTO = new BookingDTO();

        try
        {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
            bookingDTO = mapToDTO(booking);
            bookingDTO.setStatusCode(200);
            bookingDTO.setMessage("Booking fetched successfully");

        }
        catch (IllegalArgumentException e)
        {
            logger.error("Error occurred while fetching booking with ID: {}", id, e);
            bookingDTO.setStatusCode(404);
            bookingDTO.setMessage(e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching booking with ID: {}", id, e);
            bookingDTO.setStatusCode(500);
            bookingDTO.setMessage("Error occurred while fetching the booking: " + e.getMessage());
        }

        return bookingDTO;
    }


    public List<BookingDTO> getBookingsByUserId(Integer userId)
    {
        logger.info("Fetching bookings for user ID: {}", userId);
        List<BookingDTO> bookingDTOs = new ArrayList<>();

        try
        {
            SystemUser systemUser = userRepository.findById(userId).orElse(null);

            if (systemUser != null)
            {
                List<Booking> bookings = bookingRepository.findBySystemUser(systemUser);

                if (!bookings.isEmpty())
                {
                    bookingDTOs = bookings.stream()
                            .map(this::mapToDTO)
                            .collect(Collectors.toList());
                }
                else
                {
                    logger.warn("No bookings found for user ID: {}", userId);
                }
            }
            else
            {
                logger.warn("SystemUser not found with ID: {}", userId);
            }

        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching bookings for user ID: {}", userId, e);
        }

        return bookingDTOs;
    }

    public RoomAvailabilityDTO saveRoomAvailability(RoomAvailabilityDTO requestDTO)
    {
        logger.info("Saving room availability for check-in: {}, check-out: {}", requestDTO.getCheckIn(), requestDTO.getCheckOut());
        List<RoomAvailability> roomAvailabilityList = new ArrayList<>();

        try
        {
            for (RoomAvailabilityDTO.RoomTypeDTO roomTypeDTO : requestDTO.getRoomTypes())
            {
                RoomAvailability roomAvailability = new RoomAvailability();
                roomAvailability.setCheckIn(requestDTO.getCheckIn());
                roomAvailability.setCheckOut(requestDTO.getCheckOut());
                roomAvailability.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());

                RoomType roomType = roomTypeRepository.findById(roomTypeDTO.getRoomTypeID())
                        .orElseThrow(() -> new IllegalArgumentException("RoomType not found"));
                roomAvailability.setRoomType(roomType);

                roomAvailabilityList.add(roomAvailability);
            }

            roomAvailabilityRepository.saveAll(roomAvailabilityList);

            requestDTO.setStatusCode(200);
            requestDTO.setMessage("Room availability saved successfully");

        }
        catch (IllegalArgumentException e)
        {
            logger.error("Error occurred while saving room availability", e);
            requestDTO.setStatusCode(400);
            requestDTO.setMessage(e.getMessage());
        }
        catch (Exception e)
        {
            logger.error("Error occurred while saving room availability", e);
            requestDTO.setStatusCode(500);
            requestDTO.setMessage("Error occurred while saving room availability: " + e.getMessage());
        }

        return requestDTO;
    }
}