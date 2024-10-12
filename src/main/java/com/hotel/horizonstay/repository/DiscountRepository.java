package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Discount;
import com.hotel.horizonstay.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findBySeasonId(Long seasonID);

    Collection<Discount> findDiscountsBySeason(Season highestMarkupSeason);
}
