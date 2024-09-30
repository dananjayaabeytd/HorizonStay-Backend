package com.hotel.horizonstay.helper;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.dto.UserDTO;

public class Validation {

    // Helper method to validate hotel data
    public boolean isInvalidHotelData(HotelDTO hotelDTO)
    {
        return hotelDTO.getHotelName() == null || hotelDTO.getHotelName().isEmpty() ||
                hotelDTO.getHotelDescription() == null || hotelDTO.getHotelDescription().isEmpty() ||
                hotelDTO.getHotelContactNumber() == null || hotelDTO.getHotelContactNumber().isEmpty() ||
                hotelDTO.getHotelCity() == null || hotelDTO.getHotelCity().isEmpty() ||
                hotelDTO.getHotelCountry() == null || hotelDTO.getHotelCountry().isEmpty() ||
                hotelDTO.getHotelRating() == null || hotelDTO.getHotelImages().isEmpty();
    }

    public boolean isInvalidUserData(UserDTO reg) {
        return false;
    }
}
