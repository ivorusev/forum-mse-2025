package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.PageResult;
import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.exception.ResourceNotFoundException;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.entity.UserEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import com.edu.pwc.forum.service.mappers.TopicMapper;
import com.edu.pwc.forum.service.mappers.TopicsMapper;
import jakarta.transaction.Transactional;
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
    private final UserService userService;
    private final VoteService voteService;

    public TopicResponse createTopic(TopicRequest request) {
        TopicEntity topicEntity = topicMapper.requestToEntity(request);
        UserEntity author = userService.findByUsername(request.getUsername());
        topicEntity.setUser(author);
        author.getTopics().add(topicEntity);
        topicEntity = topicRepository.save(topicEntity);
        return TopicsMapper.mapToDTOWithVotes(topicEntity, voteService);
    }

    public TopicEntity findByTitle(String title) {
        return topicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Topic %s does not exist", title)));
    }

    public PageResult<TopicResponse> getAllTopics(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<TopicEntity> topics = topicRepository.findAll(pageable);
        List<TopicResponse> topicsDTO = topics.getContent().stream()
                .map(topic -> TopicsMapper.mapToDTOWithVotes(topic, voteService))
                .toList();

        PageResult<TopicResponse> pageResult = new PageResult<>();
        pageResult.setContent(topicsDTO);
        pageResult.setPage(topics.getNumber());
        pageResult.setSize(topics.getSize());
        pageResult.setTotalElements(topics.getTotalElements());
        pageResult.setTotalPages(topics.getTotalPages());
        pageResult.setEmpty(topics.isEmpty());
        return pageResult;
    }

    @Transactional
    public void deleteByTitle(String title) {
        topicRepository.findByTitle(title).ifPresent(topic -> {
            topic.getUser().getTopics().remove(topic);
            topicRepository.delete(topic);
        });
    }
}
