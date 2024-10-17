package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Cacheable(value = "hotels", unless = "#result.statusCode != 200")
    public HotelDTO getAllHotels()
    {
        HotelDTO res = new HotelDTO();

        try
        {

            List<Hotel> result = hotelRepository.findAll();

            if (!result.isEmpty())
            {
                res.setHotelList(result);
                res.setStatusCode(200);
                res.setMessage("Hotels retrieved successfully");

            } else
            {
                res.setStatusCode(404);
                res.setMessage("No hotels found");
            }

            return res;

        } catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            return res;
        }
    }

    @Cacheable(value = "hotels", key = "#hotelID", unless = "#result.statusCode != 200")
    public HotelDTO getHotelById(Long hotelID)
    {
        HotelDTO res = new HotelDTO();
        try
        {
            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);

            if (hotelOptional.isPresent())
            {
                Hotel hotel = hotelOptional.get();
                res.setHotelID(hotel.getHotelID());
                res.setHotelName(hotel.getHotelName());
                res.setHotelEmail(hotel.getHotelEmail());
                res.setHotelDescription(hotel.getHotelDescription());
                res.setHotelContactNumber(hotel.getHotelContactNumber());
                res.setHotelCity(hotel.getHotelCity());
                res.setHotelCountry(hotel.getHotelCountry());
                res.setHotelRating(hotel.getHotelRating());
                res.setHotelImages(hotel.getHotelImages());
                res.setStatusCode(200);
                res.setMessage("Hotel found successfully");
            } else
            {
                res.setStatusCode(404);
                res.setMessage("Hotel not found");
            }
        } catch (Exception e)
        {
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }

        return res;
    }

    @Caching(
            put = {
                    @CachePut(value = "hotels", key = "#result.hotelID", unless = "#result.statusCode != 200"),
                    @CachePut(value = "hotels", key = "'all'", unless = "#result.statusCode != 200")
            }
    )
    public HotelDTO addHotel(HotelDTO hotelDTO) {
        HotelDTO res = new HotelDTO();

        try {
            // Check if hotel already exists with the same name or email
            Optional<Hotel> existingHotelByName = hotelRepository.findByHotelName(hotelDTO.getHotelName());
            Optional<Hotel> existingHotelByEmail = hotelRepository.findByHotelEmail(hotelDTO.getHotelEmail());

            if (existingHotelByName.isPresent() || existingHotelByEmail.isPresent()) {
                res.setMessage("Hotel already exists with same name or email");
                res.setStatusCode(409); // Conflict
                return res;
            }

            // Create and map the Hotel entity
            Hotel hotel = new Hotel();
            hotel.setHotelName(hotelDTO.getHotelName());
            hotel.setHotelEmail(hotelDTO.getHotelEmail());
            hotel.setHotelDescription(hotelDTO.getHotelDescription());
            hotel.setHotelContactNumber(hotelDTO.getHotelContactNumber()); // Set contact number
            hotel.setHotelCity(hotelDTO.getHotelCity());
            hotel.setHotelCountry(hotelDTO.getHotelCountry());
            hotel.setHotelRating(hotelDTO.getHotelRating());
            hotel.setHotelImages(hotelDTO.getHotelImages());

            // Save the hotel in the repository
            Hotel savedHotel = hotelRepository.save(hotel);

            // Map saved hotel data back to DTO
            res.setHotelID(savedHotel.getHotelID());
            res.setHotelName(savedHotel.getHotelName());
            res.setHotelDescription(savedHotel.getHotelDescription());
            res.setHotelContactNumber(savedHotel.getHotelContactNumber()); // Return contact number
            res.setHotelCity(savedHotel.getHotelCity());
            res.setHotelEmail(savedHotel.getHotelEmail());
            res.setHotelCountry(savedHotel.getHotelCountry());
            res.setHotelRating(savedHotel.getHotelRating());
            res.setHotelImages(savedHotel.getHotelImages());

            // Set success response
            res.setStatusCode(200);
            res.setMessage("Hotel added successfully");
        } catch (Exception e) {
            // Handle any errors
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }
        return res;
    }

//    public HotelDTO addHotel(HotelDTO hotelDTO)
//    {
//        HotelDTO res = new HotelDTO();
//
//        try {
//            // Create and map the Hotel entity
//            Hotel hotel = new Hotel();
//            hotel.setHotelName(hotelDTO.getHotelName());
//            hotel.setHotelEmail(hotelDTO.getHotelEmail());
//            hotel.setHotelDescription(hotelDTO.getHotelDescription());
//            hotel.setHotelContactNumber(hotelDTO.getHotelContactNumber()); // Set contact number
//            hotel.setHotelCity(hotelDTO.getHotelCity());
//            hotel.setHotelCountry(hotelDTO.getHotelCountry());
//            hotel.setHotelRating(hotelDTO.getHotelRating());
//            hotel.setHotelImages(hotelDTO.getHotelImages());
//
//            // Save the hotel in the repository
//            Hotel savedHotel = hotelRepository.save(hotel);
//
//            // Map saved hotel data back to DTO
//            res.setHotelID(savedHotel.getHotelID());
//            res.setHotelName(savedHotel.getHotelName());
//            res.setHotelDescription(savedHotel.getHotelDescription());
//            res.setHotelContactNumber(savedHotel.getHotelContactNumber()); // Return contact number
//            res.setHotelCity(savedHotel.getHotelCity());
//            res.setHotelEmail(savedHotel.getHotelEmail());
//            res.setHotelCountry(savedHotel.getHotelCountry());
//            res.setHotelRating(savedHotel.getHotelRating());
//            res.setHotelImages(savedHotel.getHotelImages());
//
//            // Set success response
//            res.setStatusCode(200);
//            res.setMessage("Hotel added successfully");
//        } catch (Exception e)
//        {
//            // Handle any errors
//            res.setStatusCode(500);
//            res.setError("Error occurred: " + e.getMessage());
//        }
//        return res;
//    }


    @Caching(
            put = {
                    @CachePut(value = "hotels", key = "#hotelID", unless = "#result.statusCode != 200"),
                    @CachePut(value = "hotels", key = "'all'", unless = "#result.statusCode != 200")
            }
    )
    @CachePut(value = "hotels", key = "#hotelID")
    public HotelDTO updateHotel(Long hotelID, HotelDTO hotelDTO)
    {
        HotelDTO res = new HotelDTO();
        try
        {
            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);
            if (hotelOptional.isPresent())
            {
                Hotel hotel = hotelOptional.get();
                hotel.setHotelName(hotelDTO.getHotelName());
                hotel.setHotelEmail(hotelDTO.getHotelEmail());
                hotel.setHotelDescription(hotelDTO.getHotelDescription());
                hotel.setHotelContactNumber(hotelDTO.getHotelContactNumber());
                hotel.setHotelCity(hotelDTO.getHotelCity());
                hotel.setHotelCountry(hotelDTO.getHotelCountry());
                hotel.setHotelRating(hotelDTO.getHotelRating());
                hotel.setHotelImages(hotelDTO.getHotelImages());

                Hotel updatedHotel = hotelRepository.save(hotel);

                res.setHotelID(updatedHotel.getHotelID());
                res.setHotelName(updatedHotel.getHotelName());
                res.setHotelDescription(updatedHotel.getHotelDescription());
                res.setHotelContactNumber(updatedHotel.getHotelContactNumber());
                res.setHotelCity(updatedHotel.getHotelCity());
                res.setHotelEmail(updatedHotel.getHotelEmail());
                res.setHotelCountry(updatedHotel.getHotelCountry());
                res.setHotelRating(updatedHotel.getHotelRating());
                res.setHotelImages(updatedHotel.getHotelImages());
                res.setStatusCode(200);
                res.setMessage("Hotel updated successfully");
            } else
            {
                res.setStatusCode(404);
                res.setMessage("Hotel not found for update");
            }
        } catch (Exception e)
        {
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }
        return res;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "hotels", key = "#hotelID"),
                    @CacheEvict(value = "hotels", key = "'all'")
            }
    )
    public HotelDTO deleteHotel(Long hotelID)
    {
        HotelDTO res = new HotelDTO();

        try
        {
            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);
            if (hotelOptional.isPresent())
            {
                hotelRepository.deleteById(hotelID);
                res.setStatusCode(200);
                res.setMessage("Hotel deleted successfully");
            } else
            {
                res.setStatusCode(404);
                res.setMessage("Hotel not found for deletion");
            }
        } catch (Exception e)
        {
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }

        return res;
    }
}
