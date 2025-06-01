package com.edu.pwc.forum.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.pwc.forum.persistence.entity.TopicEntity;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
}
