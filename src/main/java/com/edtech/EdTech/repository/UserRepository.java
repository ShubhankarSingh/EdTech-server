package com.edtech.EdTech.repository;

import com.edtech.EdTech.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}
