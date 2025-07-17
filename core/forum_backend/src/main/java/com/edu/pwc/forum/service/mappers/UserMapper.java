package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.UserResponse;
import org.mapstruct.Mapper;
import com.edu.pwc.forum.api.dtos.UserRequest;
import com.edu.pwc.forum.persistence.entity.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity requestToEntity(UserRequest request);
    UserResponse entityToResponse(UserEntity entity);
}