package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.ReplyRequest;
import com.edu.pwc.forum.api.dtos.ReplyResponse;
import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReplyMapper {

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "modifiedOn", ignore = true)
    ReplyEntity requestToEntity(ReplyRequest request);

    @Mapping(source = "topic.title", target = "topicTitle")
    @Mapping(source = "user.username", target = "authorUsername")
    ReplyResponse entityToResponse(ReplyEntity entity);
}