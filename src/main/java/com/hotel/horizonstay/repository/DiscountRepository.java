package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
