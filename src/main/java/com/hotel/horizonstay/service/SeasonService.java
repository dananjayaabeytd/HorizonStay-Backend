package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.SeasonDTO;
import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.ContractRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeasonService {

    private static final Logger logger = LoggerFactory.getLogger(SeasonService.class);

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private ContractRepository contractRepository;

    public SeasonDTO addSeasonToContract(Long contractId, SeasonDTO seasonDTO)
    {
        logger.info("Adding season to contract with ID: {}", contractId);
        SeasonDTO res = new SeasonDTO();

        try
        {
            Optional<HotelContract> contractOptional = contractRepository.findById(contractId);

            if (contractOptional.isPresent())
            {
                Season season = new Season();
                season.setSeasonName(seasonDTO.getSeasonName());
                season.setValidFrom(seasonDTO.getValidFrom());
                season.setValidTo(seasonDTO.getValidTo());
                season.setContract(contractOptional.get());
                season = seasonRepository.save(season);

                res = convertToDTO(season);
                res.setStatusCode(200);
                res.setMessage("Season added successfully");
                logger.info("Season added successfully to contract with ID: {}", contractId);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Contract not found");
                logger.warn("Contract with ID: {} not found", contractId);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while adding season to contract with ID: {}", contractId, e);
        }

        return res;
    }

    public SeasonDTO getSeasonById(Long seasonID)
    {
        logger.info("Fetching season with ID: {}", seasonID);
        SeasonDTO res = new SeasonDTO();

        try
        {
            Optional<Season> season = seasonRepository.findById(seasonID);

            if (season.isPresent())
            {
                res = convertToDTO(season.get());
                res.setStatusCode(200);
                res.setMessage("Season found successfully");
                logger.info("Season with ID: {} found successfully", seasonID);
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
            logger.error("Error occurred while fetching season with ID: {}", seasonID, e);
        }

        return res;
    }

    public SeasonDTO updateSeason(Long seasonID, SeasonDTO seasonDTO)
    {
        logger.info("Updating season with ID: {}", seasonID);
        SeasonDTO res = new SeasonDTO();

        try
        {
            Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

            if (seasonOptional.isPresent())
            {
                Season season = seasonOptional.get();
                season.setSeasonName(seasonDTO.getSeasonName());
                season.setValidFrom(seasonDTO.getValidFrom());
                season.setValidTo(seasonDTO.getValidTo());
                season = seasonRepository.save(season);

                res = convertToDTO(season);
                res.setStatusCode(200);
                res.setMessage("Season updated successfully");
                logger.info("Season with ID: {} updated successfully", seasonID);
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
            logger.error("Error occurred while updating season with ID: {}", seasonID, e);
        }

        return res;
    }

    public SeasonDTO deleteSeason(Long seasonID)
    {
        logger.info("Deleting season with ID: {}", seasonID);
        SeasonDTO res = new SeasonDTO();

        try
        {
            Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

            if (seasonOptional.isPresent())
            {
                seasonRepository.deleteById(seasonID);
                res.setStatusCode(200);
                res.setMessage("Season deleted successfully");
                logger.info("Season with ID: {} deleted successfully", seasonID);
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
            logger.error("Error occurred while deleting season with ID: {}", seasonID, e);
        }

        return res;
    }

    public List<SeasonDTO> getSeasonsByContractId(Long contractID)
    {
        logger.info("Fetching seasons for contract with ID: {}", contractID);
        List<SeasonDTO> res;

        try
        {
            List<Season> seasons = seasonRepository.findByContractId(contractID);
            res = seasons.stream().map(this::convertToDTO).collect(Collectors.toList());
            logger.info("Fetched {} seasons for contract with ID: {}", res.size(), contractID);
        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching seasons for contract with ID: {}", contractID, e);
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return res;
    }

    private SeasonDTO convertToDTO(Season season)
    {
        SeasonDTO seasonDTO = new SeasonDTO();

        seasonDTO.setId(season.getId());
        seasonDTO.setSeasonName(season.getSeasonName());
        seasonDTO.setValidFrom(season.getValidFrom());
        seasonDTO.setValidTo(season.getValidTo());

        return seasonDTO;
    }
}