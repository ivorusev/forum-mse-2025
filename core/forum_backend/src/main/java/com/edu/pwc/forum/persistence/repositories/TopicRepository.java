package com.edu.pwc.forum.persistence.repositories;

import com.edu.pwc.forum.persistence.entity.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    Optional<TopicEntity> findByTitle(String topicTitle);
    Page<TopicEntity> findByCategoryId(Long categoryId, Pageable pageable);
}
