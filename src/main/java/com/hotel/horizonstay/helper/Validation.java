package com.hotel.horizonstay.helper;

import com.hotel.horizonstay.dto.HotelContractDTO;
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

    // Helper method to validate user data
    public boolean isInvalidUserData(UserDTO user)
    {
        return user.getName() == null || user.getName().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getAddress() == null || user.getAddress().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getRole() == null || user.getRole().isEmpty() ||
                user.getImage() == null || user.getImage().isEmpty() ||
                user.getNIC() == null || user.getNIC().isEmpty();
    }

    public boolean isInvalidContractData(HotelContractDTO contractDTO) {
    return false;
    }
}
