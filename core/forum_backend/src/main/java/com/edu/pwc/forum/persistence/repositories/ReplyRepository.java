package com.edu.pwc.forum.persistence.repositories;

import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    Page<ReplyEntity> findByTopicId(Long topicId, Pageable pageable);
}