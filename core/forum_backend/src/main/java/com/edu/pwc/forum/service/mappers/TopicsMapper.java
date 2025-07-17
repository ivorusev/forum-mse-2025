package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.persistence.entity.TopicEntity;

public class TopicsMapper {

    public static TopicResponse mapToDTO(TopicEntity topic) {
        return new TopicResponse(topic.getId(), topic.getTitle(), topic.getCreatedOn(), topic.getModifiedOn(), topic.getUser().getUsername());
    }
}
