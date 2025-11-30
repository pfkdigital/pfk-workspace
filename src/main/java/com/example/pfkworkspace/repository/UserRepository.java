package com.example.pfkworkspace.repository;

import com.example.pfkworkspace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmailAddress(String email);
    Optional<User> findUserByProviderId(String providerId);
    boolean existsByEmailAddress(String email);
    boolean existsByUsername(String username);
}
