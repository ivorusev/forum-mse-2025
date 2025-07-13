package com.edu.pwc.forum.persistence.repositories;

import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    Page<ReplyEntity> findByTopicEntity_Id(Long topicId, Pageable pageable);
}