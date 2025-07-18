package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.VoteResponse;
import com.edu.pwc.forum.persistence.entity.VoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "reply.id", target = "replyId")
    VoteResponse entityToResponse(VoteEntity entity);
}
