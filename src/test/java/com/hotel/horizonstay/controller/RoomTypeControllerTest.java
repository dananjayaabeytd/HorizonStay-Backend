package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class RoomTypeControllerTest {

    @Mock
    private RoomTypeService roomTypeService;

    @InjectMocks
    private RoomTypeController roomTypeController;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRoomTypeToSeason_Success()
    {
        // Arrange
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Deluxe Room");
        MultipartFile[] files = {};

        when(roomTypeService.addRoomTypeToSeason(anyLong(), any(RoomTypeDTO.class))).thenReturn(roomTypeDTO);

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.addRoomTypeToSeason(1L, files, roomTypeDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Deluxe Room", Objects.requireNonNull(response.getBody()).getRoomTypeName());
    }




    @Test
    void testAddRoomTypeToSeason_Exception()
    {
        // Arrange
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Deluxe Room");
        MultipartFile[] files = {};

        when(roomTypeService.addRoomTypeToSeason(anyLong(), any(RoomTypeDTO.class))).thenThrow(new RuntimeException("Error Adding room type"));

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.addRoomTypeToSeason(1L, files, roomTypeDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding room type", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetRoomTypeById_Success()
    {
        // Arrange
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Deluxe Room");

        when(roomTypeService.getRoomTypeById(anyLong())).thenReturn(roomTypeDTO);

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.getRoomTypeById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deluxe Room", Objects.requireNonNull(response.getBody()).getRoomTypeName());
    }

    @Test
    void testGetRoomTypeById_NotFound()
    {
        // Arrange
        when(roomTypeService.getRoomTypeById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.getRoomTypeById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room type not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetRoomTypeById_Exception()
    {
        // Arrange
        when(roomTypeService.getRoomTypeById(anyLong())).thenThrow(new RuntimeException("Error Getting room type by ID"));

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.getRoomTypeById(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching room type", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateRoomType_Success()
    {
        // Arrange
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Updated Room");
        MultipartFile[] files = {};

        when(roomTypeService.updateRoomType(anyLong(), any(RoomTypeDTO.class))).thenReturn(roomTypeDTO);

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.updateRoomType(1L, files, roomTypeDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Room", Objects.requireNonNull(response.getBody()).getRoomTypeName());
    }

    @Test
    void testUpdateRoomType_NotFound()
    {
        // Arrange
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Updated Room");
        MultipartFile[] files = {};

        when(roomTypeService.updateRoomType(anyLong(), any(RoomTypeDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.updateRoomType(1L, files, roomTypeDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room type not found for update", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateRoomType_Exception()
    {
        // Arrange
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeName("Updated Room");
        MultipartFile[] files = {};

        when(roomTypeService.updateRoomType(anyLong(), any(RoomTypeDTO.class))).thenThrow(new RuntimeException("Error Updating room type"));

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.updateRoomType(1L, files, roomTypeDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating room type", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteRoomType_Success()
    {
        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.deleteRoomType(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteRoomType_NotFound()
    {
        // Arrange
        doThrow(new IllegalArgumentException("Room type not found")).when(roomTypeService).deleteRoomType(anyLong());

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.deleteRoomType(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room type not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteRoomType_Exception()
    {
        // Arrange
        doThrow(new RuntimeException("Error Deleting room type")).when(roomTypeService).deleteRoomType(anyLong());

        // Act
        ResponseEntity<RoomTypeDTO> response = roomTypeController.deleteRoomType(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting room type", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetRoomTypesBySeasonId_Success()
    {
        // Arrange
        RoomTypeDTO roomType1 = new RoomTypeDTO();
        roomType1.setRoomTypeName("Deluxe Room");
        RoomTypeDTO roomType2 = new RoomTypeDTO();
        roomType2.setRoomTypeName("Standard Room");

        List<RoomTypeDTO> roomTypes = Arrays.asList(roomType1, roomType2);
        when(roomTypeService.getRoomTypesBySeasonId(anyLong())).thenReturn(roomTypes);

        // Act
        ResponseEntity<List<RoomTypeDTO>> response = roomTypeController.getRoomTypesBySeasonId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetRoomTypesBySeasonId_Exception()
    {
        // Arrange
        when(roomTypeService.getRoomTypesBySeasonId(anyLong())).thenThrow(new RuntimeException("Error Getting room types by season ID"));

        // Act
        ResponseEntity<List<RoomTypeDTO>> response = roomTypeController.getRoomTypesBySeasonId(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}