package com.edu.pwc.forum.service.mappers;

import org.mapstruct.Mapper;
import com.edu.pwc.forum.api.dtos.UserRequest;
import com.edu.pwc.forum.persistence.entity.UserEntity;

@Mapper
public interface UserMapper {
    UserEntity requestToEntity(UserRequest request);
}