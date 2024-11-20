package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.MarkupDTO;
import com.hotel.horizonstay.entity.Markup;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.MarkupRepository;
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

class MarkupServiceTest {

    @Mock
    private MarkupRepository markupRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @InjectMocks
    private MarkupService markupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMarkupToSeason_Success() {
        Long seasonId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("Holiday");
        markupDTO.setPercentage(10.0);

        Season season = new Season();
        season.setId(seasonId);

        Markup savedMarkup = new Markup();
        savedMarkup.setPercentage(10.0f); // Set a non-null value for percentage

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));
        when(markupRepository.findByMarkupNameAndSeasonId(markupDTO.getMarkupName(), seasonId)).thenReturn(Optional.empty());
        when(markupRepository.save(any(Markup.class))).thenReturn(savedMarkup);

        MarkupDTO result = markupService.addMarkupToSeason(seasonId, markupDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Markup added successfully", result.getMessage());
    }

    @Test
    void addMarkupToSeason_SeasonNotFound() {
        Long seasonId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.empty());

        MarkupDTO result = markupService.addMarkupToSeason(seasonId, markupDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Season not found", result.getMessage());
    }

    @Test
    void addMarkupToSeason_MarkupAlreadyExists() {
        Long seasonId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("Holiday");

        Season season = new Season();
        season.setId(seasonId);

        Markup existingMarkup = new Markup();
        existingMarkup.setMarkupName("Holiday");

        when(seasonRepository.findById(seasonId)).thenReturn(Optional.of(season));
        when(markupRepository.findByMarkupNameAndSeasonId(markupDTO.getMarkupName(), seasonId)).thenReturn(Optional.of(existingMarkup));

        MarkupDTO result = markupService.addMarkupToSeason(seasonId, markupDTO);

        assertEquals(409, result.getStatusCode());
        assertEquals("Markup with the same name already exists in the season", result.getMessage());
    }

    @Test
    void addMarkupToSeason_Error() {
        Long seasonId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();

        when(seasonRepository.findById(seasonId)).thenThrow(new RuntimeException("Database error"));

        MarkupDTO result = markupService.addMarkupToSeason(seasonId, markupDTO);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getMarkupById_Success() {
        Long markupId = 1L;
        Markup markup = new Markup();
        markup.setId(markupId);
        markup.setPercentage(10.0f); // Set a non-null value for percentage

        when(markupRepository.findById(markupId)).thenReturn(Optional.of(markup));

        MarkupDTO result = markupService.getMarkupById(markupId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Markup found successfully", result.getMessage());
    }

    @Test
    void getMarkupById_NotFound() {
        Long markupId = 1L;

        when(markupRepository.findById(markupId)).thenReturn(Optional.empty());

        MarkupDTO result = markupService.getMarkupById(markupId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Markup not found", result.getMessage());
    }

    @Test
    void getMarkupById_Error() {
        Long markupId = 1L;

        when(markupRepository.findById(markupId)).thenThrow(new RuntimeException("Database error"));

        MarkupDTO result = markupService.getMarkupById(markupId);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void updateMarkup_Success() {
        Long markupId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("Holiday");
        markupDTO.setPercentage(15.0);

        Markup markup = new Markup();
        markup.setId(markupId);

        when(markupRepository.findById(markupId)).thenReturn(Optional.of(markup));
        when(markupRepository.save(any(Markup.class))).thenReturn(markup);

        MarkupDTO result = markupService.updateMarkup(markupId, markupDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Markup updated successfully", result.getMessage());
    }

    @Test
    void updateMarkup_NotFound() {
        Long markupId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();

        when(markupRepository.findById(markupId)).thenReturn(Optional.empty());

        MarkupDTO result = markupService.updateMarkup(markupId, markupDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Markup not found", result.getMessage());
    }

    @Test
    void updateMarkup_Error() {
        Long markupId = 1L;
        MarkupDTO markupDTO = new MarkupDTO();

        when(markupRepository.findById(markupId)).thenThrow(new RuntimeException("Database error"));

        MarkupDTO result = markupService.updateMarkup(markupId, markupDTO);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void deleteMarkup_Success() {
        Long markupId = 1L;
        Markup markup = new Markup();
        markup.setId(markupId);

        when(markupRepository.findById(markupId)).thenReturn(Optional.of(markup));

        MarkupDTO result = markupService.deleteMarkup(markupId);

        assertEquals(200, result.getStatusCode());
        assertEquals("Markup deleted successfully", result.getMessage());
    }

    @Test
    void deleteMarkup_NotFound() {
        Long markupId = 1L;

        when(markupRepository.findById(markupId)).thenReturn(Optional.empty());

        MarkupDTO result = markupService.deleteMarkup(markupId);

        assertEquals(404, result.getStatusCode());
        assertEquals("Markup not found", result.getMessage());
    }

    @Test
    void deleteMarkup_Error() {
        Long markupId = 1L;

        when(markupRepository.findById(markupId)).thenThrow(new RuntimeException("Database error"));

        MarkupDTO result = markupService.deleteMarkup(markupId);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getMarkupsBySeasonId_Success() {
        Long seasonId = 1L;
        Markup markup = new Markup();
        markup.setId(1L);
        markup.setPercentage(10.0f); // Set a non-null value for percentage

        when(markupRepository.findBySeasonId(seasonId)).thenReturn(Arrays.asList(markup));

        List<MarkupDTO> result = markupService.getMarkupsBySeasonId(seasonId);

        assertEquals(1, result.size());
    }

    @Test
    void getMarkupsBySeasonId_Error() {
        Long seasonId = 1L;

        when(markupRepository.findBySeasonId(seasonId)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            markupService.getMarkupsBySeasonId(seasonId);
        });

        assertEquals("Error occurred: Database error", exception.getMessage());
    }
}