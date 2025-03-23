package com.edu.pwc.forum.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pwc.forum.persistence.TopicService;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HelloController {

	private final TopicService topicService;

}