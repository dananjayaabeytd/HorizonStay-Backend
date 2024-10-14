package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.SeasonDTO;
import com.hotel.horizonstay.service.SeasonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class SeasonControllerTest {

    @Mock
    private SeasonService seasonService;

    @InjectMocks
    private SeasonController seasonController;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSeasonToContract_Success()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Summer Season");
        when(seasonService.addSeasonToContract(anyLong(), any(SeasonDTO.class))).thenReturn(seasonDTO);

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.addSeasonToContract(1L, seasonDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Summer Season", Objects.requireNonNull(response.getBody()).getSeasonName());
    }

    @Test
    void testAddSeasonToContract_InvalidData()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.addSeasonToContract(1L, seasonDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAddSeasonToContract_Exception()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Summer Season");
        when(seasonService.addSeasonToContract(anyLong(), any(SeasonDTO.class))).thenThrow(new RuntimeException("Error Adding season"));

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.addSeasonToContract(1L, seasonDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding season", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetSeasonById_Success()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Summer Season");
        when(seasonService.getSeasonById(anyLong())).thenReturn(seasonDTO);

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.getSeasonById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Summer Season", Objects.requireNonNull(response.getBody()).getSeasonName());
    }

    @Test
    void testGetSeasonById_NotFound()
    {
        // Arrange
        when(seasonService.getSeasonById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.getSeasonById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Season not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetSeasonById_Exception()
    {
        // Arrange
        when(seasonService.getSeasonById(anyLong())).thenThrow(new RuntimeException("Error Getting season by ID"));

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.getSeasonById(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching season", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateSeason_Success()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Updated Season");
        when(seasonService.updateSeason(anyLong(), any(SeasonDTO.class))).thenReturn(seasonDTO);

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.updateSeason(1L, seasonDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Season", Objects.requireNonNull(response.getBody()).getSeasonName());
    }

    @Test
    void testUpdateSeason_NotFound()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Updated Season");
        when(seasonService.updateSeason(anyLong(), any(SeasonDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.updateSeason(1L, seasonDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Season not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateSeason_Exception()
    {
        // Arrange
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Updated Season");
        when(seasonService.updateSeason(anyLong(), any(SeasonDTO.class))).thenThrow(new RuntimeException("Error Updating season"));

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.updateSeason(1L, seasonDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating season", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteSeason_Success()
    {
        // Act
        ResponseEntity<SeasonDTO> response = seasonController.deleteSeason(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteSeason_NotFound()
    {
        // Arrange
        doThrow(new IllegalArgumentException("Season not found")).when(seasonService).deleteSeason(anyLong());

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.deleteSeason(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Season not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteSeason_Exception()
    {
        // Arrange
        doThrow(new RuntimeException("Error Deleting season")).when(seasonService).deleteSeason(anyLong());

        // Act
        ResponseEntity<SeasonDTO> response = seasonController.deleteSeason(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting season", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetSeasonsByContractId_Success()
    {
        // Arrange
        SeasonDTO season1 = new SeasonDTO();
        season1.setSeasonName("Summer Season");
        SeasonDTO season2 = new SeasonDTO();
        season2.setSeasonName("Winter Season");

        List<SeasonDTO> seasons = Arrays.asList(season1, season2);
        when(seasonService.getSeasonsByContractId(anyLong())).thenReturn(seasons);

        // Act
        ResponseEntity<List<SeasonDTO>> response = seasonController.getSeasonsByContractId(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetSeasonsByContractId_Exception()
    {
        // Arrange
        when(seasonService.getSeasonsByContractId(anyLong())).thenThrow(new RuntimeException("Error Getting seasons by contract ID"));

        // Act
        ResponseEntity<List<SeasonDTO>> response = seasonController.getSeasonsByContractId(1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}