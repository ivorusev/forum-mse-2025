package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.VoteRequest;
import com.edu.pwc.forum.api.dtos.VoteResponse;
import com.edu.pwc.forum.persistence.entity.VoteEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    VoteEntity requestToEntity(VoteRequest request);

    VoteResponse entityToResponse(VoteEntity entity);
}