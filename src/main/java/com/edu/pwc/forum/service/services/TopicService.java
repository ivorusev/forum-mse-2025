package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.PageResult;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.service.mappers.TopicsMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import com.edu.pwc.forum.service.mappers.TopicMapper;
import lombok.RequiredArgsConstructor;

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
}
