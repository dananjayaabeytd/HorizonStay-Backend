package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Markup;
import com.hotel.horizonstay.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface MarkupRepository extends JpaRepository<Markup, Long> {

    List<Markup> findBySeasonId(Long seasonID);

    List<Markup> findMarkupsBySeason(Season season);

    Optional<Markup> findByMarkupNameAndSeasonId(String markupName, Long seasonID);
}
