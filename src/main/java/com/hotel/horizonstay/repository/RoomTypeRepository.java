package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.RoomType;
import com.hotel.horizonstay.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long>
{
    List<RoomType> findBySeasonId(Long seasonID);

    List<RoomType> findRoomTypesBySeason(Season highestMarkupSeason);

    Optional<RoomType> findByRoomTypeNameAndSeasonId(String roomTypeName, Long seasonId);
}
