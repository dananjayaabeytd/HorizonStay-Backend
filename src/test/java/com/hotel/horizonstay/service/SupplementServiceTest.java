package com.hotel.horizonstay.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hotel.horizonstay.dto.SupplementDTO;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.entity.Supplement;
import com.hotel.horizonstay.repository.SeasonRepository;
import com.hotel.horizonstay.repository.SupplementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

public class SupplementServiceTest {

    @Mock
    private SupplementRepository supplementRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @InjectMocks
    private SupplementService supplementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSupplementToSeason_shouldAddSuccessfully() {
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Breakfast");
        supplementDTO.setPrice(10.0F);

        Season season = new Season();
        season.setId(1L);

        when(seasonRepository.findById(1L)).thenReturn(Optional.of(season));
        when(supplementRepository.findBySupplementNameAndSeasonId("Breakfast", 1L)).thenReturn(Optional.empty());
        when(supplementRepository.save(any(Supplement.class))).thenReturn(new Supplement());

        SupplementDTO response = supplementService.addSupplementToSeason(1L, supplementDTO);

        assertEquals(200, response.getStatusCode());
        assertEquals("Supplement added successfully", response.getMessage());
    }

    @Test
    void addSupplementToSeason_shouldReturnConflictWhenSupplementExists() {
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Breakfast");

        Season season = new Season();
        season.setId(1L);

        when(seasonRepository.findById(1L)).thenReturn(Optional.of(season));
        when(supplementRepository.findBySupplementNameAndSeasonId("Breakfast", 1L)).thenReturn(Optional.of(new Supplement()));

        SupplementDTO response = supplementService.addSupplementToSeason(1L, supplementDTO);

        assertEquals(409, response.getStatusCode());
        assertEquals("Supplement with the same name already exists in the season", response.getMessage());
    }

    @Test
    void addSupplementToSeason_shouldReturnNotFoundWhenSeasonDoesNotExist() {
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Breakfast");

        when(seasonRepository.findById(1L)).thenReturn(Optional.empty());

        SupplementDTO response = supplementService.addSupplementToSeason(1L, supplementDTO);

        assertEquals(404, response.getStatusCode());
        assertEquals("Season not found", response.getMessage());
    }

    @Test
    void getSupplementById_shouldReturnSupplementSuccessfully() {
        Supplement supplement = new Supplement();
        supplement.setId(1L);
        supplement.setSupplementName("Breakfast");
        supplement.setPrice(10.0F);

        when(supplementRepository.findById(1L)).thenReturn(Optional.of(supplement));

        SupplementDTO response = supplementService.getSupplementById(1L);

        assertEquals(200, response.getStatusCode());
        assertEquals("Supplement found successfully", response.getMessage());
    }

    @Test
    void getSupplementById_shouldReturnNotFoundWhenSupplementDoesNotExist() {
        when(supplementRepository.findById(1L)).thenReturn(Optional.empty());

        SupplementDTO response = supplementService.getSupplementById(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("Supplement not found", response.getMessage());
    }

    @Test
    void updateSupplement_shouldUpdateSuccessfully() {
        Supplement supplement = new Supplement();
        supplement.setId(1L);
        supplement.setSupplementName("Breakfast");
        supplement.setPrice(10.0F);

        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Lunch");
        supplementDTO.setPrice(15.0F);

        when(supplementRepository.findById(1L)).thenReturn(Optional.of(supplement));
        when(supplementRepository.save(any(Supplement.class))).thenReturn(supplement);

        SupplementDTO response = supplementService.updateSupplement(1L, supplementDTO);

        assertEquals(200, response.getStatusCode());
        assertEquals("Supplement updated successfully", response.getMessage());
    }

    @Test
    void updateSupplement_shouldReturnNotFoundWhenSupplementDoesNotExist() {
        SupplementDTO supplementDTO = new SupplementDTO();
        supplementDTO.setSupplementName("Lunch");
        supplementDTO.setPrice(15.0F);

        when(supplementRepository.findById(1L)).thenReturn(Optional.empty());

        SupplementDTO response = supplementService.updateSupplement(1L, supplementDTO);

        assertEquals(404, response.getStatusCode());
        assertEquals("Supplement not found", response.getMessage());
    }

    @Test
    void deleteSupplement_shouldDeleteSuccessfully() {
        Supplement supplement = new Supplement();
        supplement.setId(1L);

        when(supplementRepository.findById(1L)).thenReturn(Optional.of(supplement));

        SupplementDTO response = supplementService.deleteSupplement(1L);

        assertEquals(200, response.getStatusCode());
        assertEquals("Supplement deleted successfully", response.getMessage());
    }

    @Test
    void deleteSupplement_shouldReturnNotFoundWhenSupplementDoesNotExist() {
        when(supplementRepository.findById(1L)).thenReturn(Optional.empty());

        SupplementDTO response = supplementService.deleteSupplement(1L);

        assertEquals(404, response.getStatusCode());
        assertEquals("Supplement not found", response.getMessage());
    }

    @Test
    void getSupplementsBySeasonId_shouldReturnSupplementsSuccessfully() {
        Supplement supplement = new Supplement();
        supplement.setId(1L);
        supplement.setSupplementName("Breakfast");
        supplement.setPrice(10.0F);

        when(supplementRepository.findBySeasonId(1L)).thenReturn(List.of(supplement));

        List<SupplementDTO> response = supplementService.getSupplementsBySeasonId(1L);

        assertEquals(1, response.size());
        assertEquals("Breakfast", response.get(0).getSupplementName());
    }

    @Test
    void getSupplementsBySeasonId_shouldReturnEmptyListWhenNoSupplementsExist() {
        when(supplementRepository.findBySeasonId(1L)).thenReturn(List.of());

        List<SupplementDTO> response = supplementService.getSupplementsBySeasonId(1L);

        assertTrue(response.isEmpty());
    }
}