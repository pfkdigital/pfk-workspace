package com.example.pfkworkspace.repository;

import com.example.pfkworkspace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUsername(String username);
    boolean existsByEmailAddress(String email);
    boolean existsByUsername(String username);
}
