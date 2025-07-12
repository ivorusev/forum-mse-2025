package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicEntity requestToEntity(TopicRequest request);

    @Mapping(source = "user.username", target = "authorUsername")
    TopicResponse entityToResponse(TopicEntity entity);
}
