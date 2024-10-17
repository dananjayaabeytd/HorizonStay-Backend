package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.MarkupDTO;
import com.hotel.horizonstay.entity.Markup;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.MarkupRepository;
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
public class MarkupService {

    @Autowired
    private MarkupRepository markupRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @CachePut(value = "markups", key = "#result.id")
    public MarkupDTO addMarkupToSeason(Long seasonID, MarkupDTO markupDTO)
    {
        Optional<Season> seasonOptional = seasonRepository.findById(seasonID);

        if (seasonOptional.isPresent())
        {
            Markup markup = new Markup();
            // Set fields from markupDTO to markup
            markup.setMarkupName(markupDTO.getMarkupName());
            markup.setPercentage((float) markupDTO.getPercentage());
            markup.setSeason(seasonOptional.get());
            markup = markupRepository.save(markup);
            return convertToDTO(markup);
        } else {
            throw new IllegalArgumentException("Season not found");
        }
    }

    @Cacheable(value = "markups", key = "#markupID")
    public MarkupDTO getMarkupById(Long markupID)
    {
        Optional<Markup> markup = markupRepository.findById(markupID);
        return markup.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Markup not found"));
    }

    @CachePut(value = "markups", key = "#markupID")
    public MarkupDTO updateMarkup(Long markupID, MarkupDTO markupDTO)
    {
        Optional<Markup> markupOptional = markupRepository.findById(markupID);

        if (markupOptional.isPresent())
        {
            Markup markup = markupOptional.get();
            // Update fields from markupDTO to markup
            markup.setMarkupName(markupDTO.getMarkupName());
            markup.setPercentage((float) markupDTO.getPercentage());
            markup = markupRepository.save(markup);
            return convertToDTO(markup);

        } else {
            throw new IllegalArgumentException("Markup not found");
        }
    }

    @CacheEvict(value = "markups", key = "#markupID")
    public void deleteMarkup(Long markupID)
    {
        markupRepository.deleteById(markupID);
    }

    @Cacheable(value = "markupsBySeason", key = "#seasonID")
    public List<MarkupDTO> getMarkupsBySeasonId(Long seasonID)
    {
        List<Markup> markups = markupRepository.findBySeasonId(seasonID);
        return markups.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private MarkupDTO convertToDTO(Markup markup)
    {
        MarkupDTO markupDTO = new MarkupDTO();
        // Set fields from markup to markupDTO
        markupDTO.setId(markup.getId());
        markupDTO.setMarkupName(markup.getMarkupName());
        markupDTO.setPercentage(markup.getPercentage());
        return markupDTO;
    }
}