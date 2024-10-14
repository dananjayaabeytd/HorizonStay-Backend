package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.MarkupDTO;
import com.hotel.horizonstay.service.MarkupService;
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

class MarkupControllerTest {

    @Mock
    private MarkupService markupService;

    @InjectMocks
    private MarkupController markupController;

    @BeforeEach
    void setUp()
    {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMarkupToSeason_Success()
    {
        // Arrange: Create a sample MarkupDTO object
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("transport");
        markupDTO.setPercentage(10.0);

        // Mock the addMarkupToSeason method of markupService to return the sample markup
        when(markupService.addMarkupToSeason(anyLong(), any(MarkupDTO.class))).thenReturn(markupDTO);

        // Act: Call the addMarkupToSeason method of markupController
        ResponseEntity<MarkupDTO> response = markupController.addMarkupToSeason(1L, markupDTO);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("transport", Objects.requireNonNull(response.getBody()).getMarkupName());
    }

    @Test
    void testAddMarkupToSeason_InvalidData()
    {
        // Arrange: Create a MarkupDTO object with invalid data
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("");

        // Mock the addMarkupToSeason method of markupService to throw an IllegalArgumentException
        when(markupService.addMarkupToSeason(anyLong(), any(MarkupDTO.class))).thenThrow(new IllegalArgumentException("Invalid markup data"));

        // Act: Call the addMarkupToSeason method of markupController
        ResponseEntity<MarkupDTO> response = markupController.addMarkupToSeason(1L, markupDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid markup data", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testAddMarkupToSeason_Exception()
    {
        // Arrange: Create a sample MarkupDTO object
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("transport");
        markupDTO.setPercentage(10.0);

        // Mock the addMarkupToSeason method of markupService to throw an exception
        when(markupService.addMarkupToSeason(anyLong(), any(MarkupDTO.class))).thenThrow(new RuntimeException("Error Adding markup"));

        // Act: Call the addMarkupToSeason method of markupController
        ResponseEntity<MarkupDTO> response = markupController.addMarkupToSeason(1L, markupDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding markup", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetMarkupById_Success()
    {
        // Arrange: Create a sample MarkupDTO object
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("transport");
        markupDTO.setPercentage(10.0);

        // Mock the getMarkupById method of markupService to return the sample markup
        when(markupService.getMarkupById(anyLong())).thenReturn(markupDTO);

        // Act: Call the getMarkupById method of markupController
        ResponseEntity<MarkupDTO> response = markupController.getMarkupById(1L);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("transport", Objects.requireNonNull(response.getBody()).getMarkupName());
    }

    @Test
    void testGetMarkupById_NotFound()
    {
        // Mock the getMarkupById method of markupService to return null
        when(markupService.getMarkupById(anyLong())).thenReturn(null);

        // Act: Call the getMarkupById method of markupController
        ResponseEntity<MarkupDTO> response = markupController.getMarkupById(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Markup not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetMarkupById_Exception()
    {
        // Mock the getMarkupById method of markupService to throw an exception
        when(markupService.getMarkupById(anyLong())).thenThrow(new RuntimeException("Error Getting markup by ID"));

        // Act: Call the getMarkupById method of markupController
        ResponseEntity<MarkupDTO> response = markupController.getMarkupById(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while fetching markup", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateMarkup_Success()
    {
        // Arrange: Create a sample MarkupDTO object
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("transport");
        markupDTO.setPercentage(15.0);

        // Mock the updateMarkup method of markupService to return the sample markup
        when(markupService.updateMarkup(anyLong(), any(MarkupDTO.class))).thenReturn(markupDTO);

        // Act: Call the updateMarkup method of markupController
        ResponseEntity<MarkupDTO> response = markupController.updateMarkup(1L, markupDTO);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("transport", Objects.requireNonNull(response.getBody()).getMarkupName());
    }

    @Test
    void testUpdateMarkup_NotFound()
    {
        // Arrange: Create a sample MarkupDTO object
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("transport");
        markupDTO.setPercentage(15.0);

        // Mock the updateMarkup method of markupService to return null
        when(markupService.updateMarkup(anyLong(), any(MarkupDTO.class))).thenReturn(null);

        // Act: Call the updateMarkup method of markupController
        ResponseEntity<MarkupDTO> response = markupController.updateMarkup(1L, markupDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Markup not found for update", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testUpdateMarkup_Exception()
    {
        // Arrange: Create a sample MarkupDTO object
        MarkupDTO markupDTO = new MarkupDTO();
        markupDTO.setMarkupName("transport");
        markupDTO.setPercentage(15.0);

        // Mock the updateMarkup method of markupService to throw an exception
        when(markupService.updateMarkup(anyLong(), any(MarkupDTO.class))).thenThrow(new RuntimeException("Error Updating markup"));

        // Act: Call the updateMarkup method of markupController
        ResponseEntity<MarkupDTO> response = markupController.updateMarkup(1L, markupDTO);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while updating markup", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteMarkup_Success()
    {
        // Act: Call the deleteMarkup method of markupController
        ResponseEntity<MarkupDTO> response = markupController.deleteMarkup(1L);

        // Assert: Verify the response status
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteMarkup_NotFound()
    {
        // Mock the deleteMarkup method of markupService to throw an IllegalArgumentException
        doThrow(new IllegalArgumentException("Markup not found")).when(markupService).deleteMarkup(anyLong());

        // Act: Call the deleteMarkup method of markupController
        ResponseEntity<MarkupDTO> response = markupController.deleteMarkup(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Markup not found", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testDeleteMarkup_Exception()
    {
        // Mock the deleteMarkup method of markupService to throw an exception
        doThrow(new RuntimeException("Error Deleting markup")).when(markupService).deleteMarkup(anyLong());

        // Act: Call the deleteMarkup method of markupController
        ResponseEntity<MarkupDTO> response = markupController.deleteMarkup(1L);

        // Assert: Verify the response status and error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while deleting markup", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void testGetMarkupsBySeasonId_Success()
    {
        // Arrange: Create sample MarkupDTO objects
        MarkupDTO markup1 = new MarkupDTO();
        markup1.setMarkupName("transport");
        markup1.setPercentage(10.0);
        MarkupDTO markup2 = new MarkupDTO();
        markup2.setMarkupName("food");
        markup2.setPercentage(5.0);

        // Mock the getMarkupsBySeasonId method of markupService to return the sample markups
        List<MarkupDTO> markups = Arrays.asList(markup1, markup2);
        when(markupService.getMarkupsBySeasonId(anyLong())).thenReturn(markups);

        // Act: Call the getMarkupsBySeasonId method of markupController
        ResponseEntity<List<MarkupDTO>> response = markupController.getMarkupsBySeasonId(1L);

        // Assert: Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testGetMarkupsBySeasonId_Exception()
    {
        // Mock the getMarkupsBySeasonId method of markupService to throw an exception
        when(markupService.getMarkupsBySeasonId(anyLong())).thenThrow(new RuntimeException("Error Getting markups by season ID"));

        // Act: Call the getMarkupsBySeasonId method of markupController
        ResponseEntity<List<MarkupDTO>> response = markupController.getMarkupsBySeasonId(1L);

        // Assert: Verify the response status
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}