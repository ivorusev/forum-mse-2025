package com.edu.pwc.forum.service.mappers;

import org.mapstruct.Mapper;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.persistence.entity.TopicEntity;

@Mapper
public interface TopicMapper {

	TopicEntity requestToEntity(TopicRequest request);
}
