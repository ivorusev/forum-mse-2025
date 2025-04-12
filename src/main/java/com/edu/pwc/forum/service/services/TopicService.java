package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import com.edu.pwc.forum.service.mappers.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public void save(TopicRequest request) {
        TopicEntity topicEntity = topicMapper.requestToEntity(request);
        ReplyEntity reply = new ReplyEntity();
        reply.setReplyBody("asd");
        topicEntity.setReplies(List.of(reply));
        topicRepository.save(topicEntity);
    }
}
