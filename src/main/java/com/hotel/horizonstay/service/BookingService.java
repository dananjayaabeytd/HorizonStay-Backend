package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.BookingDTO;
import com.hotel.horizonstay.dto.BookingItemDTO;
import com.hotel.horizonstay.dto.RoomAvailabilityDTO;
import com.hotel.horizonstay.entity.*;
import com.hotel.horizonstay.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    RoomAvailabilityRepository roomAvailabilityRepository;


    // Method to retrieve bookings by email with exception handling
//    public List<BookingDTO> getBookingsByEmail(String email) {
//
//        BookingDTO res = new BookingDTO();
//        try {
//            List<Booking> bookings = bookingRepository.findByEmail(email);
//            if (bookings.isEmpty()) {
//                res.setStatusCode(404);
//                res.setMessage("No bookings found for the given email");
//            } else {
//                List<BookingDTO> bookingDTOs = bookings.stream()
//                        .map(this::mapToDTO)
//                        .collect(Collectors.toList());
//                res.setBookingList(bookingDTOs); // Assuming you have a list of bookings in your DTO
//                res.setStatusCode(200);
//                res.setMessage("Bookings retrieved successfully");
//            }
//        } catch (Exception e) {
//            res.setStatusCode(500);
//            res.setMessage("Error occurred while retrieving bookings: " + e.getMessage());
//        }
//        return (List<BookingDTO>) res;
//    }
//
//    private BookingDTO mapToDTO(Booking booking) {
//
//        BookingDTO bookingDTO = new BookingDTO();
//
//        bookingDTO.setHotelID(booking.getHotelID());
//        bookingDTO.setFullName(booking.getFullName());
//        bookingDTO.setTelephone(booking.getTelephone());
//        bookingDTO.setEmail(booking.getEmail());
//        bookingDTO.setAddress(booking.getAddress());
//        bookingDTO.setCity(booking.getCity());
//        bookingDTO.setCountry(booking.getCountry());
//        bookingDTO.setCheckIn(booking.getCheckIn().toString());
//        bookingDTO.setCheckOut(booking.getCheckOut().toString());
//        bookingDTO.setNoOfAdults(booking.getNoOfAdults());
//        bookingDTO.setNoOfChildren(booking.getNoOfChildren());
//        bookingDTO.setDiscount(booking.getDiscount());
//        bookingDTO.setPayableAmount(booking.getPayableAmount());
//
//
//
//        // Map the items
//        List<BookingItemDTO> items = booking.getItems().stream().map(item -> {
//            BookingItemDTO itemDTO = new BookingItemDTO();
//            itemDTO.setName(item.getName());
//            itemDTO.setPrice(item.getPrice());
//            itemDTO.setQuantity(item.getQuantity());
//            itemDTO.setTotalAmount(item.getTotalAmount());
//            return itemDTO;
//        }).collect(Collectors.toList());
//        bookingDTO.setItems(items);
//
//        return bookingDTO;
//    }

    public List<BookingDTO> getBookingsByEmail(String email) {
        List<BookingDTO> bookingDTOs = new ArrayList<>();
        try {
            List<Booking> bookings = bookingRepository.findByEmail(email);
            if (!bookings.isEmpty()) {
                bookingDTOs = bookings.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Handle exception if needed
        }
        return bookingDTOs;
    }

    private BookingDTO mapToDTO(Booking booking) {
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

        // Fetch hotel details using hotelID
        Hotel hotel = hotelRepository.findById(booking.getHotelID()).orElse(null);
        if (hotel != null) {
            bookingDTO.setHotelName(hotel.getHotelName());
            bookingDTO.setHotelLocation(hotel.getHotelCity() + ", " + hotel.getHotelCountry());
            bookingDTO.setHotelDescription(hotel.getHotelDescription());
            bookingDTO.setHotelContactNumber(hotel.getHotelContactNumber());
            bookingDTO.setHotelEmail(hotel.getHotelEmail());
            bookingDTO.setHotelRating(hotel.getHotelRating());
            bookingDTO.setHotelImages(hotel.getHotelImages());
        }

        // Map the items
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

//    public BookingDTO saveBooking(BookingDTO bookingDTO) {
//        BookingDTO res = new BookingDTO();
//        try {
//            // Create and populate the Booking entity from BookingDTO
//            Booking booking = new Booking();
//            booking.setFullName(bookingDTO.getFullName());
//            booking.setTelephone(bookingDTO.getTelephone());
//            booking.setEmail(bookingDTO.getEmail());
//            booking.setHotelID(bookingDTO.getHotelID());
//            booking.setAddress(bookingDTO.getAddress());
//            booking.setCity(bookingDTO.getCity());
//            booking.setCountry(bookingDTO.getCountry());
//            booking.setCheckIn(LocalDate.parse(bookingDTO.getCheckIn()));
//            booking.setCheckOut(LocalDate.parse(bookingDTO.getCheckOut()));
//            booking.setNoOfAdults(bookingDTO.getNoOfAdults());
//            booking.setNoOfChildren(bookingDTO.getNoOfChildren());
//            booking.setDiscount(bookingDTO.getDiscount());
//            booking.setPayableAmount(bookingDTO.getPayableAmount());
//
//            // Map BookingItemDTOs to BookingItems
//            List<BookingItem> bookingItems = new ArrayList<>();
//            for (BookingItemDTO itemDTO : bookingDTO.getItems()) {
//                BookingItem item = new BookingItem();
//                item.setName(itemDTO.getName());
//                item.setPrice(itemDTO.getPrice());
//                item.setQuantity(itemDTO.getQuantity());
//                item.setTotalAmount(itemDTO.getTotalAmount());
//                item.setBooking(booking);
//                bookingItems.add(item);
//            }
//
//            booking.setItems(bookingItems);
//
//            // Save the Booking entity
//            Booking savedBooking = bookingRepository.save(booking);
//
//            // Map saved Booking entity to BookingDTO for response
//            BookingDTO savedBookingDTO = mapToDTO(savedBooking);
//            savedBookingDTO.setStatusCode(200);
//            savedBookingDTO.setMessage("Booking saved successfully");
////            res.setBooking(savedBookingDTO); // Assuming you have a single booking in your DTO
//
//            return savedBookingDTO;
//
//        } catch (Exception e) {
//            res.setStatusCode(500);
//            res.setMessage("Error occurred while saving booking: " + e.getMessage());
//        }
//        return res;
//    }

    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        BookingDTO res = new BookingDTO();

        try {
            // Create and populate the Booking entity from BookingDTO
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

            // Fetch and set the SystemUser entity
            if (bookingDTO.getSystemUserId() != null) {
                SystemUser systemUser = userRepository.findById(bookingDTO.getSystemUserId()).orElse(null);
                booking.setSystemUser(systemUser);
            }

            // Map BookingItemDTOs to BookingItems
            List<BookingItem> bookingItems = new ArrayList<>();
            for (BookingItemDTO itemDTO : bookingDTO.getItems()) {
                BookingItem item = new BookingItem();
                item.setName(itemDTO.getName());
                item.setPrice(itemDTO.getPrice());
                item.setQuantity(itemDTO.getQuantity());
                item.setTotalAmount(itemDTO.getTotalAmount());
                item.setBooking(booking);
                bookingItems.add(item);
            }

            booking.setItems(bookingItems);

            // Save the Booking entity
            Booking savedBooking = bookingRepository.save(booking);

            // Map saved Booking entity to BookingDTO for response
            BookingDTO savedBookingDTO = mapToDTO(savedBooking);
            savedBookingDTO.setStatusCode(200);
            savedBookingDTO.setMessage("Booking saved successfully");

            return savedBookingDTO;

        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage("Error occurred while saving booking: " + e.getMessage());
        }
        return res;
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        return null;
    }

    public void deleteBooking(Long id) {

    }

    public BookingDTO getBookingById(Long id) {
        BookingDTO bookingDTO = new BookingDTO();
        try {
            Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
            bookingDTO = mapToDTO(booking);
        } catch (IllegalArgumentException e) {
            bookingDTO.setStatusCode(404);
            bookingDTO.setMessage(e.getMessage());
        } catch (Exception e) {
            bookingDTO.setStatusCode(500);
            bookingDTO.setMessage("Error occurred while fetching the booking: " + e.getMessage());
        }
        return bookingDTO;
    }

    public List<BookingDTO> getBookingsByUserId(Integer userId) {
        List<BookingDTO> bookingDTOs = new ArrayList<>();
        try {
            SystemUser systemUser = userRepository.findById(userId).orElse(null);
            if (systemUser != null) {
                List<Booking> bookings = bookingRepository.findBySystemUser(systemUser);
                if (!bookings.isEmpty()) {
                    bookingDTOs = bookings.stream()
                            .map(this::mapToDTO)
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            // need to handle Exception
        }
        return bookingDTOs;
    }

    public RoomAvailabilityDTO saveRoomAvailability(RoomAvailabilityDTO requestDTO) {
        List<RoomAvailability> roomAvailabilityList = new ArrayList<>();

        for (RoomAvailabilityDTO.RoomTypeDTO roomTypeDTO : requestDTO.getRoomTypes()) {
            RoomAvailability roomAvailability = new RoomAvailability();
            roomAvailability.setCheckIn(requestDTO.getCheckIn());
            roomAvailability.setCheckOut(requestDTO.getCheckOut());
            roomAvailability.setNumberOfRooms(roomTypeDTO.getNumberOfRooms()); // Corrected method name

            RoomType roomType = roomTypeRepository.findById(roomTypeDTO.getRoomTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("RoomType not found"));
            roomAvailability.setRoomType(roomType);

            roomAvailabilityList.add(roomAvailability);
        }

        roomAvailabilityRepository.saveAll(roomAvailabilityList);

        requestDTO.setStatusCode(200);
        requestDTO.setMessage("Room availability saved successfully");

        return requestDTO;
    }
}
