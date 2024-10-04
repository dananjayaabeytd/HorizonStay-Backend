package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.DiscountDTO;
import com.hotel.horizonstay.entity.Discount;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.DiscountRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public DiscountDTO addDiscountToSeason(Long seasonID, DiscountDTO discountDTO) {
        Optional<Season> seasonOptional = seasonRepository.findById(seasonID);
        if (seasonOptional.isPresent()) {
            Discount discount = new Discount();
            // Set fields from discountDTO to discount
            discount.setDiscountName(discountDTO.getDiscountName());
            discount.setPercentage(discountDTO.getPercentage());
            discount.setSeason(seasonOptional.get());
            discount = discountRepository.save(discount);
            return convertToDTO(discount);
        } else {
            throw new IllegalArgumentException("Season not found");
        }
    }

    public DiscountDTO getDiscountById(Long discountID) {
        Optional<Discount> discount = discountRepository.findById(discountID);
        return discount.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Discount not found"));
    }

    public DiscountDTO updateDiscount(Long discountID, DiscountDTO discountDTO) {
        Optional<Discount> discountOptional = discountRepository.findById(discountID);
        if (discountOptional.isPresent()) {
            Discount discount = discountOptional.get();
            // Update fields from discountDTO to discount
            discount.setDiscountName(discountDTO.getDiscountName());
            discount.setPercentage(discountDTO.getPercentage());
            discount = discountRepository.save(discount);
            return convertToDTO(discount);
        } else {
            throw new IllegalArgumentException("Discount not found");
        }
    }

    public void deleteDiscount(Long discountID) {
        discountRepository.deleteById(discountID);
    }

    public List<DiscountDTO> getDiscountsBySeasonId(Long seasonID) {
        List<Discount> discounts = discountRepository.findBySeasonId(seasonID);
        return discounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DiscountDTO convertToDTO(Discount discount) {
        DiscountDTO discountDTO = new DiscountDTO();
        // Set fields from discount to discountDTO
        discountDTO.setId(discount.getId());
        discountDTO.setDiscountName(discount.getDiscountName());
        discountDTO.setPercentage(discount.getPercentage());
        return discountDTO;
    }
}