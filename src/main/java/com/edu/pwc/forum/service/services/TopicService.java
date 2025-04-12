package com.edu.pwc.forum.service.services;

import org.springframework.stereotype.Service;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import com.edu.pwc.forum.service.mappers.TopicMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {

	private final TopicRepository topicRepository;
	private final TopicMapper topicMapper;

	public void save(TopicRequest request) {
		TopicEntity topicEntity = topicMapper.requestToEntity(request);
		topicRepository.save(topicEntity);
	}
}
