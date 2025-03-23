package com.edu.pwc.forum.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.service.services.TopicService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

	private final TopicService topicService;

	@PostMapping
	public void createTopic(@RequestBody TopicRequest request) {
    	System.out.println(request);
		topicService.save(request);
	}

}