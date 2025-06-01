package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.UserRequest;
import com.edu.pwc.forum.persistence.entity.UserEntity;
import com.edu.pwc.forum.persistence.repositories.UserRepository;
import com.edu.pwc.forum.service.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        UserEntity entity = userMapper.requestToEntity(request);
        userRepository.save(entity);
    }
}
