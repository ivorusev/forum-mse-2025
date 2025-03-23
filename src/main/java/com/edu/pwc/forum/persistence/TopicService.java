package com.edu.pwc.forum.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pwc.forum.persistence.entity.TopicEntity;
import com.edu.pwc.forum.persistence.repositories.TopicRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
public class TopicService {

	// @Autowired
	// private TopicRepository topicRepository;
	//
	// public void save(TopicEntity entity) {
	// 	topicRepository.save(entity);
	// }
}
