package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Season;
import com.hotel.horizonstay.entity.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplementRepository extends JpaRepository<Supplement, Long>
{
    List<Supplement> findBySeasonId(Long seasonID);

    Collection<Supplement> findSupplementsBySeason(Season highestMarkupSeason);

    Optional<Supplement> findBySupplementNameAndSeasonId(String supplementName, Long seasonID);
}
