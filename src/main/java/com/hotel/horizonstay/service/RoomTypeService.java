package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.entity.RoomType;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.RoomTypeRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RoomTypeDTO addRoomTypeToSeason(Long seasonId, RoomTypeDTO roomTypeDTO) {
        Optional<Season> seasonOptional = seasonRepository.findById(seasonId);
        if (seasonOptional.isPresent()) {
            RoomType roomType = new RoomType();
            // Set fields from roomTypeDTO to roomType
            roomType.setSeason(seasonOptional.get());
            roomType = roomTypeRepository.save(roomType);
            return convertToDTO(roomType);
        } else {
            throw new IllegalArgumentException("Season not found");
        }
    }

    public RoomTypeDTO getRoomTypeById(Long roomTypeID) {
        Optional<RoomType> roomType = roomTypeRepository.findById(roomTypeID);
        return roomType.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Room type not found"));
    }

    public RoomTypeDTO updateRoomType(Long roomTypeID, RoomTypeDTO roomTypeDTO) {
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(roomTypeID);
        if (roomTypeOptional.isPresent()) {
            RoomType roomType = roomTypeOptional.get();
            // Update fields from roomTypeDTO to roomType
            roomType = roomTypeRepository.save(roomType);
            return convertToDTO(roomType);
        } else {
            throw new IllegalArgumentException("Room type not found");
        }
    }

    public void deleteRoomType(Long roomTypeID) {
        roomTypeRepository.deleteById(roomTypeID);
    }

    public List<RoomTypeDTO> getRoomTypesBySeasonId(Long seasonID) {
        List<RoomType> roomTypes = roomTypeRepository.findBySeasonId(seasonID);
        return roomTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private RoomTypeDTO convertToDTO(RoomType roomType) {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        // Set fields from roomType to roomTypeDTO
        return roomTypeDTO;
    }
}