package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.SupplementDTO;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.entity.Supplement;
import com.hotel.horizonstay.repository.SeasonRepository;
import com.hotel.horizonstay.repository.SupplementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplementService {

    private static final Logger logger = LoggerFactory.getLogger(SupplementService.class);

    @Autowired
    private SupplementRepository supplementRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public SupplementDTO addSupplementToSeason(Long seasonID, SupplementDTO supplementDTO)
    {
        logger.info("Adding supplement to season with ID: {}", seasonID);
        SupplementDTO res = new SupplementDTO();

        try
        {
            Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

            if (seasonOptional.isPresent())
            {
                Supplement supplement = new Supplement();
                supplement.setSupplementName(supplementDTO.getSupplementName());
                supplement.setPrice(supplementDTO.getPrice());
                supplement.setSeason(seasonOptional.get());

                supplement = supplementRepository.save(supplement);
                res = convertToDTO(supplement);

                res.setStatusCode(200);
                res.setMessage("Supplement added successfully");
                logger.info("Supplement added successfully to season with ID: {}", seasonID);

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
            logger.error("Error occurred while adding supplement to season with ID: {}", seasonID, e);
        }

        return res;
    }

    public SupplementDTO getSupplementById(Long supplementID)
    {
        logger.info("Fetching supplement with ID: {}", supplementID);
        SupplementDTO res = new SupplementDTO();

        try
        {
            Optional<Supplement> supplement = supplementRepository.findById(supplementID);

            if (supplement.isPresent())
            {
                res = convertToDTO(supplement.get());
                res.setStatusCode(200);
                res.setMessage("Supplement found successfully");
                logger.info("Supplement with ID: {} found successfully", supplementID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Supplement not found");
                logger.warn("Supplement with ID: {} not found", supplementID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while fetching supplement with ID: {}", supplementID, e);
        }

        return res;
    }

    public SupplementDTO updateSupplement(Long supplementID, SupplementDTO supplementDTO)
    {
        logger.info("Updating supplement with ID: {}", supplementID);
        SupplementDTO res = new SupplementDTO();

        try
        {
            Optional<Supplement> supplementOptional = supplementRepository.findById(supplementID);

            if (supplementOptional.isPresent())
            {
                Supplement supplement = supplementOptional.get();
                supplement.setSupplementName(supplementDTO.getSupplementName());
                supplement.setPrice(supplementDTO.getPrice());
                supplement = supplementRepository.save(supplement);

                res = convertToDTO(supplement);
                res.setStatusCode(200);
                res.setMessage("Supplement updated successfully");
                logger.info("Supplement with ID: {} updated successfully", supplementID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Supplement not found");
                logger.warn("Supplement with ID: {} not found", supplementID);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while updating supplement with ID: {}", supplementID, e);
        }

        return res;
    }

    public SupplementDTO deleteSupplement(Long supplementID)
    {
        logger.info("Deleting supplement with ID: {}", supplementID);
        SupplementDTO res = new SupplementDTO();

        try
        {
            Optional<Supplement> supplementOptional = supplementRepository.findById(supplementID);

            if (supplementOptional.isPresent())
            {
                supplementRepository.deleteById(supplementID);

                res.setStatusCode(200);
                res.setMessage("Supplement deleted successfully");
                logger.info("Supplement with ID: {} deleted successfully", supplementID);

            } else
            {
                res.setStatusCode(404);
                res.setMessage("Supplement not found");
                logger.warn("Supplement with ID: {} not found", supplementID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while deleting supplement with ID: {}", supplementID, e);
        }

        return res;
    }

    public List<SupplementDTO> getSupplementsBySeasonId(Long seasonID)
    {
        logger.info("Fetching supplements for season with ID: {}", seasonID);
        List<SupplementDTO> res;

        try
        {
            List<Supplement> supplements = supplementRepository.findBySeasonId(seasonID);
            res = supplements.stream().map(this::convertToDTO).collect(Collectors.toList());

            logger.info("Fetched {} supplements for season with ID: {}", res.size(), seasonID);
        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching supplements for season with ID: {}", seasonID, e);
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return res;
    }

    private SupplementDTO convertToDTO(Supplement supplement)
    {
        SupplementDTO supplementDTO = new SupplementDTO();

        supplementDTO.setSupplementID(supplement.getId());
        supplementDTO.setSupplementName(supplement.getSupplementName());
        supplementDTO.setPrice(supplement.getPrice());

        return supplementDTO;
    }
}