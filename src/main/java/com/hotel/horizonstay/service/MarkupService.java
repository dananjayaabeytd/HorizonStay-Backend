package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.MarkupDTO;
import com.hotel.horizonstay.entity.Markup;
import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.repository.MarkupRepository;
import com.hotel.horizonstay.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public MarkupDTO addMarkupToSeason(Long seasonID, MarkupDTO markupDTO) {
        Optional<Season> seasonOptional = seasonRepository.findById(seasonID);
        if (seasonOptional.isPresent()) {
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

    public MarkupDTO getMarkupById(Long markupID) {
        Optional<Markup> markup = markupRepository.findById(markupID);
        return markup.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Markup not found"));
    }

    public MarkupDTO updateMarkup(Long markupID, MarkupDTO markupDTO) {
        Optional<Markup> markupOptional = markupRepository.findById(markupID);
        if (markupOptional.isPresent()) {
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

    public void deleteMarkup(Long markupID) {
        markupRepository.deleteById(markupID);
    }

    public List<MarkupDTO> getMarkupsBySeasonId(Long seasonID) {
        List<Markup> markups = markupRepository.findBySeasonId(seasonID);
        return markups.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private MarkupDTO convertToDTO(Markup markup) {
        MarkupDTO markupDTO = new MarkupDTO();
        // Set fields from markup to markupDTO
        markupDTO.setId(markup.getId());
        markupDTO.setMarkupName(markup.getMarkupName());
        markupDTO.setPercentage(markup.getPercentage());
        return markupDTO;
    }
}