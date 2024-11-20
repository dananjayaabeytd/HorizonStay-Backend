package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.DiscountDTO;
import com.hotel.horizonstay.entity.Discount;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.DiscountRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDiscountToSeason_Success() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Summer Sale");
        discountDTO.setPercentage(10);
        Season season = new Season();
        season.setId(1L);
        when(seasonRepository.findById(1L)).thenReturn(Optional.of(season));
        when(discountRepository.findByDiscountNameAndSeasonId(any(), any())).thenReturn(Optional.empty());

        Discount discount = new Discount();
        discount.setPercentage(10.0f); // Ensure percentage is set
        when(discountRepository.save(any())).thenReturn(discount);

        DiscountDTO result = discountService.addDiscountToSeason(1L, discountDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Discount added successfully", result.getMessage());
    }

    @Test
    void addDiscountToSeason_Conflict() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Summer Sale");
        discountDTO.setPercentage(10);
        Season season = new Season();
        season.setId(1L);
        when(seasonRepository.findById(1L)).thenReturn(Optional.of(season));
        when(discountRepository.findByDiscountNameAndSeasonId(any(), any())).thenReturn(Optional.of(new Discount()));

        DiscountDTO result = discountService.addDiscountToSeason(1L, discountDTO);

        assertEquals(409, result.getStatusCode());
        assertEquals("Discount with the same name already exists in the season", result.getMessage());
    }

    @Test
    void addDiscountToSeason_SeasonNotFound() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Summer Sale");
        discountDTO.setPercentage(10);
        when(seasonRepository.findById(1L)).thenReturn(Optional.empty());

        DiscountDTO result = discountService.addDiscountToSeason(1L, discountDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Season not found", result.getMessage());
    }

    @Test
    void addDiscountToSeason_Error() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Summer Sale");
        discountDTO.setPercentage(10);
        when(seasonRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        DiscountDTO result = discountService.addDiscountToSeason(1L, discountDTO);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getDiscountById_Success() {
        Discount discount = new Discount();
        discount.setId(1L);
        discount.setPercentage(10.0f); // Ensure percentage is set
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));

        DiscountDTO result = discountService.getDiscountById(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Discount found successfully", result.getMessage());
    }

    @Test
    void getDiscountById_NotFound() {
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        DiscountDTO result = discountService.getDiscountById(1L);

        assertEquals(404, result.getStatusCode());
        assertEquals("Discount not found", result.getMessage());
    }

    @Test
    void getDiscountById_Error() {
        when(discountRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        DiscountDTO result = discountService.getDiscountById(1L);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void updateDiscount_Success() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Winter Sale");
        discountDTO.setPercentage(15);
        Discount discount = new Discount();
        discount.setId(1L);
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        when(discountRepository.save(any())).thenReturn(discount);

        DiscountDTO result = discountService.updateDiscount(1L, discountDTO);

        assertEquals(200, result.getStatusCode());
        assertEquals("Discount updated successfully", result.getMessage());
    }

    @Test
    void updateDiscount_NotFound() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Winter Sale");
        discountDTO.setPercentage(15);
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        DiscountDTO result = discountService.updateDiscount(1L, discountDTO);

        assertEquals(404, result.getStatusCode());
        assertEquals("Discount not found", result.getMessage());
    }

    @Test
    void updateDiscount_Error() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setDiscountName("Winter Sale");
        discountDTO.setPercentage(15);
        when(discountRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        DiscountDTO result = discountService.updateDiscount(1L, discountDTO);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void deleteDiscount_Success() {
        Discount discount = new Discount();
        discount.setId(1L);
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        doNothing().when(discountRepository).deleteById(1L);

        DiscountDTO result = discountService.deleteDiscount(1L);

        assertEquals(200, result.getStatusCode());
        assertEquals("Discount deleted successfully", result.getMessage());
    }

    @Test
    void deleteDiscount_NotFound() {
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        DiscountDTO result = discountService.deleteDiscount(1L);

        assertEquals(404, result.getStatusCode());
        assertEquals("Discount not found", result.getMessage());
    }

    @Test
    void deleteDiscount_Error() {
        when(discountRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        DiscountDTO result = discountService.deleteDiscount(1L);

        assertEquals(500, result.getStatusCode());
        assertEquals("Error occurred: Database error", result.getMessage());
    }

    @Test
    void getDiscountsBySeasonId_Success() {
        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setPercentage(10.0f); // Ensure percentage is not null
        discounts.add(discount);
        when(discountRepository.findBySeasonId(1L)).thenReturn(discounts);

        List<DiscountDTO> result = discountService.getDiscountsBySeasonId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getDiscountsBySeasonId_Error() {
        when(discountRepository.findBySeasonId(1L)).thenThrow(new RuntimeException("Database error"));

        try {
            discountService.getDiscountsBySeasonId(1L);
        } catch (RuntimeException e) {
            assertEquals("Error occurred: Database error", e.getMessage());
        }
    }
}