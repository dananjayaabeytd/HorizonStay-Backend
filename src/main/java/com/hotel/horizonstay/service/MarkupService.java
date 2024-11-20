package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.MarkupDTO;
import com.hotel.horizonstay.entity.Markup;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.MarkupRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarkupService {

    private static final Logger logger = LoggerFactory.getLogger(MarkupService.class);

    @Autowired
    private MarkupRepository markupRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public MarkupDTO addMarkupToSeason(Long seasonID, MarkupDTO markupDTO)
    {
        logger.info("Adding markup to season with ID: {}", seasonID);
        MarkupDTO res = new MarkupDTO();

        try
        {
            Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

            if (seasonOptional.isPresent())
            {

                // Check if a markup with the same name already exists in the season
                Optional<Markup> existingMarkup = markupRepository.findByMarkupNameAndSeasonId(markupDTO.getMarkupName(), seasonID);

                if (existingMarkup.isPresent()) {
                    res.setStatusCode(409); // Conflict status code
                    res.setMessage("Markup with the same name already exists in the season");
                    logger.warn("Markup with name: {} already exists in season with ID: {}", markupDTO.getMarkupName(), seasonID);
                    return res;
                }

                Markup markup = new Markup();
                markup.setMarkupName(markupDTO.getMarkupName());
                markup.setPercentage((float) markupDTO.getPercentage());
                markup.setSeason(seasonOptional.get());
                markup = markupRepository.save(markup);

                res = convertToDTO(markup);

                res.setStatusCode(200);
                res.setMessage("Markup added successfully");
                logger.info("Markup added successfully to season with ID: {}", seasonID);

            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Season not found");
                logger.warn("Season with ID: {} not found", seasonID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while adding markup to season with ID: {}", seasonID, e);
        }

        return res;
    }

    public MarkupDTO getMarkupById(Long markupID)
    {
        logger.info("Fetching markup with ID: {}", markupID);
        MarkupDTO res = new MarkupDTO();

        try
        {
            Optional<Markup> markup = markupRepository.findById(markupID);

            if (markup.isPresent())
            {
                res = convertToDTO(markup.get());
                res.setStatusCode(200);
                res.setMessage("Markup found successfully");
                logger.info("Markup with ID: {} found successfully", markupID);

            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Markup not found");
                logger.warn("Markup with ID: {} not found", markupID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while fetching markup with ID: {}", markupID, e);
        }

        return res;
    }

    public MarkupDTO updateMarkup(Long markupID, MarkupDTO markupDTO)
    {
        logger.info("Updating markup with ID: {}", markupID);
        MarkupDTO res = new MarkupDTO();

        try
        {
            Optional<Markup> markupOptional = markupRepository.findById(markupID);

            if (markupOptional.isPresent())
            {
                Markup markup = markupOptional.get();
                markup.setMarkupName(markupDTO.getMarkupName());
                markup.setPercentage((float) markupDTO.getPercentage());
                markup = markupRepository.save(markup);

                res = convertToDTO(markup);
                res.setStatusCode(200);
                res.setMessage("Markup updated successfully");
                logger.info("Markup with ID: {} updated successfully", markupID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Markup not found");
                logger.warn("Markup with ID: {} not found", markupID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while updating markup with ID: {}", markupID, e);
        }

        return res;
    }

    public MarkupDTO deleteMarkup(Long markupID)
    {
        logger.info("Deleting markup with ID: {}", markupID);
        MarkupDTO res = new MarkupDTO();

        try
        {
            Optional<Markup> markupOptional = markupRepository.findById(markupID);

            if (markupOptional.isPresent())
            {
                markupRepository.deleteById(markupID);
                res.setStatusCode(200);
                res.setMessage("Markup deleted successfully");
                logger.info("Markup with ID: {} deleted successfully", markupID);
            } else
            {
                res.setStatusCode(404);
                res.setMessage("Markup not found");
                logger.warn("Markup with ID: {} not found", markupID);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while deleting markup with ID: {}", markupID, e);
        }

        return res;
    }

    public List<MarkupDTO> getMarkupsBySeasonId(Long seasonID)
    {
        logger.info("Fetching markups for season with ID: {}", seasonID);
        List<MarkupDTO> res;

        try
        {
            List<Markup> markups = markupRepository.findBySeasonId(seasonID);
            res = markups.stream().map(this::convertToDTO).collect(Collectors.toList());
            logger.info("Fetched {} markups for season with ID: {}", res.size(), seasonID);
        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching markups for season with ID: {}", seasonID, e);
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return res;
    }

    private MarkupDTO convertToDTO(Markup markup)
    {
        MarkupDTO markupDTO = new MarkupDTO();

        markupDTO.setId(markup.getId());
        markupDTO.setMarkupName(markup.getMarkupName());
        markupDTO.setPercentage(markup.getPercentage());

        return markupDTO;
    }
}