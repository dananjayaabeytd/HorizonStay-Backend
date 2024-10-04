package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Markup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkupRepository extends JpaRepository<Markup, Long> {
    List<Markup> findBySeasonId(Long seasonID);
}
