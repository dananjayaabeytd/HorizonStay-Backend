package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepository hotelRepository;

    public HotelDTO getAllHotels()
    {
        logger.info("Fetching all hotels");
        HotelDTO res = new HotelDTO();

        try
        {

            List<Hotel> result = hotelRepository.findAll();

            if (!result.isEmpty())
            {
                res.setHotelList(result);
                res.setStatusCode(200);
                res.setMessage("Hotels retrieved successfully");
                logger.info("Hotels retrieved successfully");


            } else
            {
                res.setStatusCode(404);
                res.setMessage("No hotels found");
                logger.warn("No hotels found");

            }

            return res;

        } catch (Exception e)
        {
            logger.error("Error occurred while fetching all hotels: {}", e.getMessage());
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            return res;
        }
    }

    public HotelDTO getHotelById(Long hotelID)
    {
        logger.info("Fetching hotel with ID: {}", hotelID);
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
                logger.info("Hotel with ID: {} found successfully", hotelID);

            } else
            {
                res.setStatusCode(404);
                res.setMessage("Hotel not found");
                logger.warn("Hotel with ID: {} not found", hotelID);

            }
        } catch (Exception e)
        {
            logger.error("Error occurred while fetching hotel with ID: {}: {}", hotelID, e.getMessage());
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }

        return res;
    }


    public HotelDTO addHotel(HotelDTO hotelDTO) {

        logger.info("Adding new hotel with name: {}", hotelDTO.getHotelName());
        HotelDTO res = new HotelDTO();

        try {
            // Check if hotel already exists with the same name or email
            Optional<Hotel> existingHotelByName = hotelRepository.findByHotelName(hotelDTO.getHotelName());
            Optional<Hotel> existingHotelByEmail = hotelRepository.findByHotelEmail(hotelDTO.getHotelEmail());

            if (existingHotelByName.isPresent() || existingHotelByEmail.isPresent()) {
                res.setMessage("Hotel already exists with same name or email");
                res.setStatusCode(409); // Conflict
                logger.warn("Hotel already exists with same name or email: {}", hotelDTO.getHotelName());
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
            logger.info("Hotel added successfully with name: {}", hotelDTO.getHotelName());

        } catch (Exception e) {
            // Handle any errors
            logger.error("Error occurred while adding hotel: {}", e.getMessage());
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }
        return res;
    }

    public HotelDTO updateHotel(Long hotelID, HotelDTO hotelDTO) {
        logger.info("Updating hotel with ID: {}", hotelID);
        HotelDTO res = new HotelDTO();

        try {
            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);

            if (hotelOptional.isPresent()) {
                Hotel hotel = hotelOptional.get();
                hotel.setHotelName(hotelDTO.getHotelName());
                hotel.setHotelEmail(hotelDTO.getHotelEmail());
                hotel.setHotelDescription(hotelDTO.getHotelDescription());
                hotel.setHotelContactNumber(hotelDTO.getHotelContactNumber());
                hotel.setHotelCity(hotelDTO.getHotelCity());
                hotel.setHotelCountry(hotelDTO.getHotelCountry());
                hotel.setHotelRating(hotelDTO.getHotelRating());

                // Ensure hotelImages is mutable
                List<String> mutableHotelImages = new ArrayList<>(hotel.getHotelImages());
                mutableHotelImages.clear();
                mutableHotelImages.addAll(hotelDTO.getHotelImages());
                hotel.setHotelImages(mutableHotelImages);

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
                logger.info("Hotel with ID: {} updated successfully", hotelID);

            } else {
                res.setStatusCode(404);
                res.setMessage("Hotel not found for update");
                logger.warn("Hotel with ID: {} not found for update", hotelID);
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating hotel with ID: {}: {}", hotelID, e);
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }
        return res;
    }

//    public HotelDTO updateHotel(Long hotelID, HotelDTO hotelDTO)
//    {
//        logger.info("Updating hotel with ID: {}", hotelID);
//        HotelDTO res = new HotelDTO();
//
//        try
//        {
//            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);
//
//            if (hotelOptional.isPresent())
//            {
//                Hotel hotel = hotelOptional.get();
//                hotel.setHotelName(hotelDTO.getHotelName());
//                hotel.setHotelEmail(hotelDTO.getHotelEmail());
//                hotel.setHotelDescription(hotelDTO.getHotelDescription());
//                hotel.setHotelContactNumber(hotelDTO.getHotelContactNumber());
//                hotel.setHotelCity(hotelDTO.getHotelCity());
//                hotel.setHotelCountry(hotelDTO.getHotelCountry());
//                hotel.setHotelRating(hotelDTO.getHotelRating());
//                hotel.setHotelImages(hotelDTO.getHotelImages());
//
//                Hotel updatedHotel = hotelRepository.save(hotel);
//
//                res.setHotelID(updatedHotel.getHotelID());
//                res.setHotelName(updatedHotel.getHotelName());
//                res.setHotelDescription(updatedHotel.getHotelDescription());
//                res.setHotelContactNumber(updatedHotel.getHotelContactNumber());
//                res.setHotelCity(updatedHotel.getHotelCity());
//                res.setHotelEmail(updatedHotel.getHotelEmail());
//                res.setHotelCountry(updatedHotel.getHotelCountry());
//                res.setHotelRating(updatedHotel.getHotelRating());
//                res.setHotelImages(updatedHotel.getHotelImages());
//
//                res.setStatusCode(200);
//                res.setMessage("Hotel updated successfully");
//                logger.info("Hotel with ID: {} updated successfully", hotelID);
//
//            } else
//            {
//                res.setStatusCode(404);
//                res.setMessage("Hotel not found for update");
//                logger.warn("Hotel with ID: {} not found for update", hotelID);
//
//            }
//        } catch (Exception e)
//        {
//            logger.error("Error occurred while updating hotel with ID: {}: {}", hotelID, e.getMessage());
//            res.setStatusCode(500);
//            res.setError("Error occurred: " + e.getMessage());
//        }
//        return res;
//    }


    public HotelDTO deleteHotel(Long hotelID)
    {
        logger.info("Deleting hotel with ID: {}", hotelID);
        HotelDTO res = new HotelDTO();

        try
        {
            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);
            if (hotelOptional.isPresent())
            {
                hotelRepository.deleteById(hotelID);
                res.setStatusCode(200);
                res.setMessage("Hotel deleted successfully");
                logger.info("Hotel with ID: {} deleted successfully", hotelID);

            } else
            {
                res.setStatusCode(404);
                res.setMessage("Hotel not found for deletion");
                logger.warn("Hotel with ID: {} not found for deletion", hotelID);

            }
        } catch (Exception e)
        {
            logger.error("Error occurred while deleting hotel with ID: {}: {}", hotelID, e.getMessage());
            res.setStatusCode(500);
            res.setError("Error occurred: " + e.getMessage());
        }

        return res;
    }
}
