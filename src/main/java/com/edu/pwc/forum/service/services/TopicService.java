package com.edu.pwc.forum.service.services;

import org.springframework.stereotype.Service;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {

	private final TopicRepository topicRepository;

	public void save(TopicRequest request) {
		TopicEntity entity = new TopicEntity();
		topicRepository.save(entity);
	}
}
