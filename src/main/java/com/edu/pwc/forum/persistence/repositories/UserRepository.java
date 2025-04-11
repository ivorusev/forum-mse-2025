package com.edu.pwc.forum.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edu.pwc.forum.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}