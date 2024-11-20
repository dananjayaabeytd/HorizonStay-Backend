package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.entity.RoomType;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.RoomTypeRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomTypeServiceTest {

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @InjectMocks
    private RoomTypeService roomTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRoomTypeToSeason_Success() {
        Long seasonId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Deluxe");

        Season season = new Season();
        season.setId(seasonId);

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));
        when(roomTypeRepository.findByRoomTypeNameAndSeasonId(roomTypeDTO.getRoomTypeName(), seasonId)).thenReturn(Optional.empty());
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(new RoomType());

        RoomTypeDTO result = roomTypeService.addRoomTypeToSeason(seasonId, roomTypeDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Room type added successfully", result.getMessage());
    }

    @Test
    void addRoomTypeToSeason_SeasonNotFound() {
        Long seasonId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.empty());

        RoomTypeDTO result = roomTypeService.addRoomTypeToSeason(seasonId, roomTypeDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Season not found", result.getMessage());
    }

    @Test
    void addRoomTypeToSeason_RoomTypeAlreadyExists() {
        Long seasonId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Deluxe");

        Season season = new Season();
        season.setId(seasonId);

        RoomType existingRoomType = new RoomType();
        existingRoomType.setRoomTypeName("Deluxe");

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));
        when(roomTypeRepository.findByRoomTypeNameAndSeasonId(roomTypeDTO.getRoomTypeName(), seasonId)).thenReturn(Optional.of(existingRoomType));

        RoomTypeDTO result = roomTypeService.addRoomTypeToSeason(seasonId, roomTypeDTO);

        assertEquals(409, result.getStatusCode());
        assertEquals("Room type with the same name already exists in the season", result.getMessage());
    }

    @Test
    void addRoomTypeToSeason_Error() {
        Long seasonId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();

        when(seasonRepository.findById(seasonId)).thenThrow(new RuntimeException("Database error"));

        RoomTypeDTO result = roomTypeService.addRoomTypeToSeason(seasonId, roomTypeDTO);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getRoomTypeById_Success() {
        Long roomTypeId = 1L;
        RoomType roomType = new RoomType();
        roomType.setId(roomTypeId);

        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.of(roomType));

        RoomTypeDTO result = roomTypeService.getRoomTypeById(roomTypeId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Room type found successfully", result.getMessage());
    }

    @Test
    void getRoomTypeById_NotFound() {
        Long roomTypeId = 1L;

        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.empty());

        RoomTypeDTO result = roomTypeService.getRoomTypeById(roomTypeId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Room type not found", result.getMessage());
    }

    @Test
    void getRoomTypeById_Error() {
        Long roomTypeId = 1L;

        when(roomTypeRepository.findById(roomTypeId)).thenThrow(new RuntimeException("Database error"));

        RoomTypeDTO result = roomTypeService.getRoomTypeById(roomTypeId);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void updateRoomType_Success() {
        Long roomTypeId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Deluxe");

        RoomType roomType = new RoomType();
        roomType.setId(roomTypeId);

        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.of(roomType));
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);

        RoomTypeDTO result = roomTypeService.updateRoomType(roomTypeId, roomTypeDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Room type updated successfully", result.getMessage());
    }

    @Test
    void updateRoomType_NotFound() {
        Long roomTypeId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();

        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.empty());

        RoomTypeDTO result = roomTypeService.updateRoomType(roomTypeId, roomTypeDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Room type not found", result.getMessage());
    }

    @Test
    void updateRoomType_Error() {
        Long roomTypeId = 1L;
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();

        when(roomTypeRepository.findById(roomTypeId)).thenThrow(new RuntimeException("Database error"));

        RoomTypeDTO result = roomTypeService.updateRoomType(roomTypeId, roomTypeDTO);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void deleteRoomType_Success() {
        Long roomTypeId = 1L;
        RoomType roomType = new RoomType();
        roomType.setId(roomTypeId);

        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.of(roomType));

        RoomTypeDTO result = roomTypeService.deleteRoomType(roomTypeId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Room type deleted successfully", result.getMessage());
    }

    @Test
    void deleteRoomType_NotFound() {
        Long roomTypeId = 1L;

        when(roomTypeRepository.findById(roomTypeId)).thenReturn(Optional.empty());

        RoomTypeDTO result = roomTypeService.deleteRoomType(roomTypeId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Room type not found", result.getMessage());
    }

    @Test
    void deleteRoomType_Error() {
        Long roomTypeId = 1L;

        when(roomTypeRepository.findById(roomTypeId)).thenThrow(new RuntimeException("Database error"));

        RoomTypeDTO result = roomTypeService.deleteRoomType(roomTypeId);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getRoomTypesBySeasonId_Success() {
        Long seasonId = 1L;
        RoomType roomType = new RoomType();
        roomType.setId(1L);

        when(roomTypeRepository.findBySeasonId(seasonId)).thenReturn(Arrays.asList(roomType));

        List<RoomTypeDTO> result = roomTypeService.getRoomTypesBySeasonId(seasonId);

        assertEquals(1, result.size());
    }

    @Test
    void getRoomTypesBySeasonId_Error() {
        Long seasonId = 1L;

        when(roomTypeRepository.findBySeasonId(seasonId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roomTypeService.getRoomTypesBySeasonId(seasonId);
        });

        assertEquals("Error occurred: Database error", exception.getMessage());
    }
}