package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findBySeasonId(Long seasonID);
}
