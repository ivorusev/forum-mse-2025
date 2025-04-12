package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.exception.ResourceNotFoundException;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public void save(TopicRequest request) {
        TopicEntity entity = new TopicEntity();
        entity.setTitle(request.getTitle());
        entity.setCreatedOn(Timestamp.from(Instant.now()));
        entity.setModifiedOn(Timestamp.from(Instant.now()));
        topicRepository.save(entity);
    }

    public TopicEntity getById(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic %s was not found"));
    }

    public TopicEntity findByTitle(String topicTitle) {
        return topicRepository.findByTitle(topicTitle).orElseThrow(() -> new ResourceNotFoundException("Topic %s was not found"));
    }
}
