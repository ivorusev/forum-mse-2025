package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.ReplyRequest;
import com.edu.pwc.forum.api.dtos.ReplyResponse;
import com.edu.pwc.forum.persistence.entity.ReplyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReplyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "modifiedOn", ignore = true)
    ReplyEntity requestToEntity(ReplyRequest request);

    @Mapping(source = "topicEntity.title", target = "topicTitle")
    ReplyResponse entityToResponse(ReplyEntity entity);
}