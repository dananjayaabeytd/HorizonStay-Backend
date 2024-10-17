package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.SupplementDTO;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.entity.Supplement;
import com.hotel.horizonstay.repository.SeasonRepository;
import com.hotel.horizonstay.repository.SupplementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplementService {

    @Autowired
    private SupplementRepository supplementRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @CachePut(value = "supplements", key = "#result.supplementID")
    public SupplementDTO addSupplementToSeason(Long seasonID, SupplementDTO supplementDTO)
    {
        Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

        if (seasonOptional.isPresent())
        {
            Supplement supplement = new Supplement();
            // Set fields from supplementDTO to supplement
            supplement.setSupplementName(supplementDTO.getSupplementName());
            supplement.setPrice(supplementDTO.getPrice());
            supplement.setSeason(seasonOptional.get());

            supplement = supplementRepository.save(supplement);
            return convertToDTO(supplement);

        } else {
            throw new IllegalArgumentException("Season not found");
        }
    }

    @Cacheable(value = "supplements", key = "#supplementID")
    public SupplementDTO getSupplementById(Long supplementID)
    {
        Optional<Supplement> supplement = supplementRepository.findById(supplementID);
        return supplement.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Supplement not found"));
    }

    @CachePut(value = "supplements", key = "#supplementID")
    public SupplementDTO updateSupplement(Long supplementID, SupplementDTO supplementDTO)
    {
        Optional<Supplement> supplementOptional = supplementRepository.findById(supplementID);

        if (supplementOptional.isPresent())
        {
            Supplement supplement = supplementOptional.get();
            // Update fields from supplementDTO to supplement
            supplement.setSupplementName(supplementDTO.getSupplementName());
            supplement.setPrice(supplementDTO.getPrice());
            supplement = supplementRepository.save(supplement);
            return convertToDTO(supplement);
        } else {
            throw new IllegalArgumentException("Supplement not found");
        }
    }

    @CacheEvict(value = "supplements", key = "#supplementID")
    public void deleteSupplement(Long supplementID)
    {
        supplementRepository.deleteById(supplementID);
    }

    @Cacheable(value = "supplementsBySeason", key = "#seasonID")
    public List<SupplementDTO> getSupplementsBySeasonId(Long seasonID)
    {
        List<Supplement> supplements = supplementRepository.findBySeasonId(seasonID);
        return supplements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private SupplementDTO convertToDTO(Supplement supplement)
    {
        SupplementDTO supplementDTO = new SupplementDTO();
        // Set fields from supplement to supplementDTO
        supplementDTO.setSupplementID(supplement.getId());
        supplementDTO.setSupplementName(supplement.getSupplementName());
        supplementDTO.setPrice(supplement.getPrice());
        return supplementDTO;
    }

}