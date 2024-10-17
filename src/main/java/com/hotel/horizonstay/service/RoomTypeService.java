package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.entity.RoomType;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.RoomTypeRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @CachePut(value = "roomTypes", key = "#result.roomTypeID")
    public RoomTypeDTO addRoomTypeToSeason(Long seasonId, RoomTypeDTO roomTypeDTO)
    {

        Optional<Season> seasonOptional = seasonRepository.findById(seasonId);

        if (seasonOptional.isPresent())
        {
            RoomType roomType = new RoomType();
            // Set fields from roomTypeDTO to roomType
            roomType.setRoomTypeName(roomTypeDTO.getRoomTypeName());
            roomType.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());
            roomType.setMaxNumberOfPersons(roomTypeDTO.getMaxNumberOfPersons());
            roomType.setPrice(roomTypeDTO.getPrice());
            roomType.setRoomTypeImages(roomTypeDTO.getRoomTypeImages());
            roomType.setSeason(seasonOptional.get());
            roomType = roomTypeRepository.save(roomType);
            return convertToDTO(roomType);
        } else {
            throw new IllegalArgumentException("Season not found");
        }
    }

    @Cacheable(value = "roomTypes", key = "#roomTypeID")
    public RoomTypeDTO getRoomTypeById(Long roomTypeID)
    {
        Optional<RoomType> roomType = roomTypeRepository.findById(roomTypeID);

        return roomType.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Room type not found"));
    }

    @CachePut(value = "roomTypes", key = "#roomTypeID")
    public RoomTypeDTO updateRoomType(Long roomTypeID, RoomTypeDTO roomTypeDTO)
    {
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(roomTypeID);

        if (roomTypeOptional.isPresent())
        {
            RoomType roomType = roomTypeOptional.get();
            // Update fields from roomTypeDTO to roomType
            roomType.setRoomTypeName(roomTypeDTO.getRoomTypeName());
            roomType.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());
            roomType.setMaxNumberOfPersons(roomTypeDTO.getMaxNumberOfPersons());
            roomType.setPrice(roomTypeDTO.getPrice());
            roomType.setRoomTypeImages(roomTypeDTO.getRoomTypeImages());
            roomType = roomTypeRepository.save(roomType);
            return convertToDTO(roomType);
        } else {
            throw new IllegalArgumentException("Room type not found");
        }
    }

    @CacheEvict(value = "roomTypes", key = "#roomTypeID")
    public void deleteRoomType(Long roomTypeID)
    {
        roomTypeRepository.deleteById(roomTypeID);
    }

    @Cacheable(value = "roomTypesBySeason", key = "#seasonID")
    public List<RoomTypeDTO> getRoomTypesBySeasonId(Long seasonID)
    {
        List<RoomType> roomTypes = roomTypeRepository.findBySeasonId(seasonID);

        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private RoomTypeDTO convertToDTO(RoomType roomType)
    {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        // Set fields from roomType to roomTypeDTO
        roomTypeDTO.setRoomTypeID(roomType.getId());
        roomTypeDTO.setRoomTypeName(roomType.getRoomTypeName());
        roomTypeDTO.setNumberOfRooms(roomType.getNumberOfRooms());
        roomTypeDTO.setMaxNumberOfPersons(roomType.getMaxNumberOfPersons());
        roomTypeDTO.setPrice(roomType.getPrice());
        roomTypeDTO.setRoomTypeImages(roomType.getRoomTypeImages());
        return roomTypeDTO;
    }
}