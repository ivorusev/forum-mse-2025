package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.TopicRequest;
import com.edu.pwc.forum.api.dtos.TopicResponse;
import com.edu.pwc.forum.persistence.entity.TopicEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface TopicMapper {

    @Mapping(target = "category.id", source = "categoryId")
    TopicEntity requestToEntity(TopicRequest request);

    @Mapping(source = "user.username", target = "authorUsername")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "body", target = "body")
    TopicResponse entityToResponse(TopicEntity entity);
}
