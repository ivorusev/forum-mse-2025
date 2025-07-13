package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.PageResult;
import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.exception.ResourceNotFoundException;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import com.edu.pwc.forum.service.mappers.TopicMapper;
import com.edu.pwc.forum.service.mappers.TopicsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public void save(TopicRequest request) {
        TopicEntity topicEntity = topicMapper.requestToEntity(request);
        topicRepository.save(topicEntity);
    }

    public TopicEntity findByTitle(String title) {
        return topicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Topic %s does not exist", title)));
    }

    public PageResult<TopicResponse> getAllTopics(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<TopicEntity> topics = topicRepository.findAll(pageable);
        List<TopicResponse> topicsDTO = topics.getContent().stream().map(TopicsMapper::mapToDTO).toList();

        PageResult<TopicResponse> pageResult = new PageResult<>();
        pageResult.setContent(topicsDTO);
        pageResult.setPage(topics.getNumber());
        pageResult.setSize(topics.getSize());
        pageResult.setTotalElements(topics.getTotalElements());
        pageResult.setTotalPages(topics.getTotalPages());
        pageResult.setEmpty(topics.isEmpty());
        return pageResult;
    }

    public TopicEntity findById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Topic with id %d does not exist", id)));
    }

    public TopicResponse getTopicById(Long id) {
        TopicEntity entity = topicRepository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return TopicsMapper.mapToDTO(entity);
    }
}
