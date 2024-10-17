package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.SeasonDTO;
import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.ContractRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeasonService {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private ContractRepository contractRepository;

    @CachePut(value = "seasons", key = "#result.id")
    public SeasonDTO addSeasonToContract(Long contractId, SeasonDTO seasonDTO)
    {
        Optional<HotelContract> contractOptional = contractRepository.findById(contractId);

        if (contractOptional.isPresent())
        {
            Season season = new Season();
            // Set fields from seasonDTO to season
            season.setSeasonName(seasonDTO.getSeasonName());
            season.setValidFrom(seasonDTO.getValidFrom());
            season.setValidTo(seasonDTO.getValidTo());
            season.setContract(contractOptional.get());
            season = seasonRepository.save(season);
            return convertToDTO(season);
        } else {
            throw new IllegalArgumentException("Contract not found");
        }
    }

    @Cacheable(value = "seasons", key = "#seasonID")
    public SeasonDTO getSeasonById(Long seasonID)
    {
        Optional<Season> season = seasonRepository.findById(seasonID);
        return season.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Season not found"));
    }

    @CachePut(value = "seasons", key = "#seasonID")
    public SeasonDTO updateSeason(Long seasonID, SeasonDTO seasonDTO)
    {
        Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

        if (seasonOptional.isPresent())
        {
            Season season = seasonOptional.get();
            // Update fields from seasonDTO to season
            season.setSeasonName(seasonDTO.getSeasonName());
            season.setValidFrom(seasonDTO.getValidFrom());
            season.setValidTo(seasonDTO.getValidTo());
            season = seasonRepository.save(season);
            return convertToDTO(season);
        } else {
            throw new IllegalArgumentException("Season not found");
        }
    }

    @CacheEvict(value = "seasons", key = "#seasonID")
    public void deleteSeason(Long seasonID)
    {
        seasonRepository.deleteById(seasonID);
    }

    @Cacheable(value = "seasonsByContract", key = "#contractID")
    public List<SeasonDTO> getSeasonsByContractId(Long contractID)
    {
        List<Season> seasons = seasonRepository.findByContractId(contractID);

        return seasons.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    SeasonDTO convertToDTO(Season season)
    {
        SeasonDTO seasonDTO = new SeasonDTO();
        // Set fields from season to seasonDTO
        seasonDTO.setId(season.getId());
        seasonDTO.setSeasonName(season.getSeasonName());
        seasonDTO.setValidFrom(season.getValidFrom());
        seasonDTO.setValidTo(season.getValidTo());

        return seasonDTO;
    }
}