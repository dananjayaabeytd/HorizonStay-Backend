package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.DiscountDTO;
import com.hotel.horizonstay.entity.Discount;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.DiscountRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public DiscountDTO addDiscountToSeason(Long seasonID, DiscountDTO discountDTO)
    {
        logger.info("Adding discount to season with ID: {}", seasonID);
        DiscountDTO res = new DiscountDTO();

        try
        {
            Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

            if (seasonOptional.isPresent())
            {
                Discount discount = new Discount();
                discount.setDiscountName(discountDTO.getDiscountName());
                discount.setPercentage(discountDTO.getPercentage());
                discount.setSeason(seasonOptional.get());
                discount = discountRepository.save(discount);

                res = convertToDTO(discount);

                res.setStatusCode(200);
                res.setMessage("Discount added successfully");
                logger.info("Discount added successfully to season with ID: {}", seasonID);

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
            logger.error("Error occurred while adding discount to season with ID: {}", seasonID, e);
        }

        return res;
    }

    public DiscountDTO getDiscountById(Long discountID)
    {
        logger.info("Fetching discount with ID: {}", discountID);
        DiscountDTO res = new DiscountDTO();

        try
        {
            Optional<Discount> discount = discountRepository.findById(discountID);

            if (discount.isPresent())
            {
                res = convertToDTO(discount.get());
                res.setStatusCode(200);
                res.setMessage("Discount found successfully");
                logger.info("Discount with ID: {} found successfully", discountID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Discount not found");
                logger.warn("Discount with ID: {} not found", discountID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while fetching discount with ID: {}", discountID, e);
        }

        return res;
    }

    public DiscountDTO updateDiscount(Long discountID, DiscountDTO discountDTO)
    {
        logger.info("Updating discount with ID: {}", discountID);
        DiscountDTO res = new DiscountDTO();

        try
        {
            Optional<Discount> discountOptional = discountRepository.findById(discountID);

            if (discountOptional.isPresent())
            {
                Discount discount = discountOptional.get();
                discount.setDiscountName(discountDTO.getDiscountName());
                discount.setPercentage(discountDTO.getPercentage());
                discount = discountRepository.save(discount);

                res = convertToDTO(discount);
                res.setStatusCode(200);
                res.setMessage("Discount updated successfully");
                logger.info("Discount with ID: {} updated successfully", discountID);

            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Discount not found");
                logger.warn("Discount with ID: {} not found", discountID);
            }
        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while updating discount with ID: {}", discountID, e);
        }

        return res;
    }

    public DiscountDTO deleteDiscount(Long discountID)
    {
        logger.info("Deleting discount with ID: {}", discountID);
        DiscountDTO res = new DiscountDTO();

        try
        {
            Optional<Discount> discountOptional = discountRepository.findById(discountID);

            if (discountOptional.isPresent())
            {
                discountRepository.deleteById(discountID);
                res.setStatusCode(200);
                res.setMessage("Discount deleted successfully");
                logger.info("Discount with ID: {} deleted successfully", discountID);
            }
            else
            {
                res.setStatusCode(404);
                res.setMessage("Discount not found");
                logger.warn("Discount with ID: {} not found", discountID);
            }

        }
        catch (Exception e)
        {
            res.setStatusCode(500);
            res.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while deleting discount with ID: {}", discountID, e);
        }

        return res;
    }

    public List<DiscountDTO> getDiscountsBySeasonId(Long seasonID)
    {
        logger.info("Fetching discounts for season with ID: {}", seasonID);
        List<DiscountDTO> res;

        try
        {
            List<Discount> discounts = discountRepository.findBySeasonId(seasonID);
            res = discounts.stream().map(this::convertToDTO).collect(Collectors.toList());
            logger.info("Fetched {} discounts for season with ID: {}", res.size(), seasonID);

        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching discounts for season with ID: {}", seasonID, e);
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return res;
    }

    private DiscountDTO convertToDTO(Discount discount)
    {
        DiscountDTO discountDTO = new DiscountDTO();

        discountDTO.setId(discount.getId());
        discountDTO.setDiscountName(discount.getDiscountName());
        discountDTO.setPercentage(discount.getPercentage());

        return discountDTO;
    }
}