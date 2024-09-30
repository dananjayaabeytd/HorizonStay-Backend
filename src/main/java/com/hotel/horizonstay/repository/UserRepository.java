package com.hotel.horizonstay.repository;

import com.hotel.horizonstay.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SystemUser,Integer> {

    Optional<SystemUser> findByEmail(String email);
}