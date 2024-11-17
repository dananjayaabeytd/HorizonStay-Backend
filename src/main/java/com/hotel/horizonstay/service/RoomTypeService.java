package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.entity.RoomType;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.RoomTypeRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {

    private static final Logger logger = LoggerFactory.getLogger(RoomTypeService.class);

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public RoomTypeDTO addRoomTypeToSeason(Long seasonId, RoomTypeDTO roomTypeDTO)
    {
        logger.info("Adding room type to season with ID: {}", seasonId);
        RoomTypeDTO res = new RoomTypeDTO();

        try
        {
            Optional<Season> seasonOptional = seasonRepository.findById(seasonId);

            if (seasonOptional.isPresent())
            {

                // Check if a room type with the same name already exists in the season
                Optional<RoomType> existingRoomType = roomTypeRepository.findByRoomTypeNameAndSeasonId(roomTypeDTO.getRoomTypeName(), seasonId);
                if (existingRoomType.isPresent()) {
                    res.setStatusCode(409); // Conflict status code
                    res.setMessage("Room type with the same name already exists in the season");
                    logger.warn("Room type with name: {} already exists in season with ID: {}", roomTypeDTO.getRoomTypeName(), seasonId);
                    return res;
                }

                RoomType roomType = new RoomType();
                roomType.setRoomTypeName(roomTypeDTO.getRoomTypeName());
                roomType.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());
                roomType.setMaxNumberOfPersons(roomTypeDTO.getMaxNumberOfPersons());
                roomType.setPrice(roomTypeDTO.getPrice());
                roomType.setRoomTypeImages(roomTypeDTO.getRoomTypeImages());
                roomType.setSeason(seasonOptional.get());
                roomType = roomTypeRepository.save(roomType);

                res = convertToDTO(roomType);
                res.setStatusCode(200);
                res.setMessage("Room type added successfully");
                logger.info("Room type added successfully to season with ID: {}", seasonId);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Season not found");
                logger.warn("Season with ID: {} not found", seasonId);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while adding room type to season with ID: {}", seasonId, e);
        }

        return res;
    }

    public RoomTypeDTO getRoomTypeById(Long roomTypeID)
    {
        logger.info("Fetching room type with ID: {}", roomTypeID);
        RoomTypeDTO res = new RoomTypeDTO();

        try
        {
            Optional<RoomType> roomType = roomTypeRepository.findById(roomTypeID);

            if (roomType.isPresent())
            {
                res = convertToDTO(roomType.get());
                res.setStatusCode(200);
                res.setMessage("Room type found successfully");
                logger.info("Room type with ID: {} found successfully", roomTypeID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Room type not found");
                logger.warn("Room type with ID: {} not found", roomTypeID);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while fetching room type with ID: {}", roomTypeID, e);
        }

        return res;
    }

    public RoomTypeDTO updateRoomType(Long roomTypeID, RoomTypeDTO roomTypeDTO)
    {
        logger.info("Updating room type with ID: {}", roomTypeID);
        RoomTypeDTO res = new RoomTypeDTO();

        try
        {
            Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(roomTypeID);

            if (roomTypeOptional.isPresent())
            {
                RoomType roomType = roomTypeOptional.get();
                roomType.setRoomTypeName(roomTypeDTO.getRoomTypeName());
                roomType.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());
                roomType.setMaxNumberOfPersons(roomTypeDTO.getMaxNumberOfPersons());
                roomType.setPrice(roomTypeDTO.getPrice());
                roomType.setRoomTypeImages(roomTypeDTO.getRoomTypeImages());
                roomType = roomTypeRepository.save(roomType);

                res = convertToDTO(roomType);
                res.setStatusCode(200);
                res.setMessage("Room type updated successfully");
                logger.info("Room type with ID: {} updated successfully", roomTypeID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Room type not found");
                logger.warn("Room type with ID: {} not found", roomTypeID);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while updating room type with ID: {}", roomTypeID, e);
        }

        return res;
    }

    public RoomTypeDTO deleteRoomType(Long roomTypeID)
    {
        logger.info("Deleting room type with ID: {}", roomTypeID);
        RoomTypeDTO res = new RoomTypeDTO();

        try
        {
            Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(roomTypeID);

            if (roomTypeOptional.isPresent())
            {
                roomTypeRepository.deleteById(roomTypeID);
                res.setStatusCode(200);
                res.setMessage("Room type deleted successfully");
                logger.info("Room type with ID: {} deleted successfully", roomTypeID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Room type not found");
                logger.warn("Room type with ID: {} not found", roomTypeID);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while deleting room type with ID: {}", roomTypeID, e);
        }

        return res;
    }

    public List<RoomTypeDTO> getRoomTypesBySeasonId(Long seasonID)
    {
        logger.info("Fetching room types for season with ID: {}", seasonID);
        List<RoomTypeDTO> res;

        try
        {
            List<RoomType> roomTypes = roomTypeRepository.findBySeasonId(seasonID);
            res = roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
            logger.info("Fetched {} room types for season with ID: {}", res.size(), seasonID);
        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching room types for season with ID: {}", seasonID, e);
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return res;
    }

    private RoomTypeDTO convertToDTO(RoomType roomType)
    {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();

        roomTypeDTO.setRoomTypeID(roomType.getId());
        roomTypeDTO.setRoomTypeName(roomType.getRoomTypeName());
        roomTypeDTO.setNumberOfRooms(roomType.getNumberOfRooms());
        roomTypeDTO.setMaxNumberOfPersons(roomType.getMaxNumberOfPersons());
        roomTypeDTO.setPrice(roomType.getPrice());
        roomTypeDTO.setRoomTypeImages(roomType.getRoomTypeImages());

        return roomTypeDTO;
    }
}