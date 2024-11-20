package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.SeasonDTO;
import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.ContractRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SeasonServiceTest {

    @Mock
    private SeasonRepository seasonRepository;

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private SeasonService seasonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSeasonToContract_Success() {
        Long contractId = 1L;
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Summer");
        seasonDTO.setValidFrom(LocalDate.of(2023, 6, 1));
        seasonDTO.setValidTo(LocalDate.of(2023, 8, 31));

        HotelContract contract = new HotelContract();
        contract.setId(contractId);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(seasonRepository.findByContractId(contractId)).thenReturn(Arrays.asList());
        when(seasonRepository.save(any(Season.class))).thenReturn(new Season());

        SeasonDTO result = seasonService.addSeasonToContract(contractId, seasonDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Season added successfully", result.getMessage());
    }

    @Test
    void addSeasonToContract_ContractNotFound() {
        Long contractId = 1L;
        SeasonDTO seasonDTO = new SeasonDTO();

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        SeasonDTO result = seasonService.addSeasonToContract(contractId, seasonDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Contract not found", result.getMessage());
    }

    @Test
    void addSeasonToContract_OverlappingSeason() {
        Long contractId = 1L;
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Summer");
        seasonDTO.setValidFrom(LocalDate.of(2023, 6, 1));
        seasonDTO.setValidTo(LocalDate.of(2023, 8, 31));

        HotelContract contract = new HotelContract();
        contract.setId(contractId);

        Season existingSeason = new Season();
        existingSeason.setValidFrom(LocalDate.of(2023, 7, 1));
        existingSeason.setValidTo(LocalDate.of(2023, 9, 30));

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(seasonRepository.findByContractId(contractId)).thenReturn(Arrays.asList(existingSeason));

        SeasonDTO result = seasonService.addSeasonToContract(contractId, seasonDTO);

        assertEquals(409, result.getStatusCode());
        assertEquals("Season dates are overlapping with existing seasons", result.getMessage());
    }

    @Test
    void getSeasonById_Success() {
        Long seasonId = 1L;
        Season season = new Season();
        season.setId(seasonId);

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));

        SeasonDTO result = seasonService.getSeasonById(seasonId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Season found successfully", result.getMessage());
    }

    @Test
    void getSeasonById_NotFound() {
        Long seasonId = 1L;

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.empty());

        SeasonDTO result = seasonService.getSeasonById(seasonId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Season not found", result.getMessage());
    }

    @Test
    void updateSeason_Success() {
        Long seasonId = 1L;
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setSeasonName("Winter");
        seasonDTO.setValidFrom(LocalDate.of(2023, 12, 1));
        seasonDTO.setValidTo(LocalDate.of(2024, 2, 28));

        Season season = new Season();
        season.setId(seasonId);

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));
        when(seasonRepository.save(any(Season.class))).thenReturn(season);

        SeasonDTO result = seasonService.updateSeason(seasonId, seasonDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Season updated successfully", result.getMessage());
    }

    @Test
    void updateSeason_NotFound() {
        Long seasonId = 1L;
        SeasonDTO seasonDTO = new SeasonDTO();

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.empty());

        SeasonDTO result = seasonService.updateSeason(seasonId, seasonDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Season not found", result.getMessage());
    }

    @Test
    void deleteSeason_Success() {
        Long seasonId = 1L;
        Season season = new Season();
        season.setId(seasonId);

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));

        SeasonDTO result = seasonService.deleteSeason(seasonId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Season deleted successfully", result.getMessage());
    }

    @Test
    void deleteSeason_NotFound() {
        Long seasonId = 1L;

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.empty());

        SeasonDTO result = seasonService.deleteSeason(seasonId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Season not found", result.getMessage());
    }

    @Test
    void getSeasonsByContractId_Success() {
        Long contractId = 1L;
        Season season = new Season();
        season.setId(1L);

        when(seasonRepository.findByContractId(contractId)).thenReturn(Arrays.asList(season));

        List<SeasonDTO> result = seasonService.getSeasonsByContractId(contractId);

        assertEquals(1, result.size());
    }
}