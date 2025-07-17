package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.PageResult;
import com.edu.pwc.forum.api.dtos.UserRequest;
import com.edu.pwc.forum.api.dtos.UserResponse;
import com.edu.pwc.forum.exception.ResourceNotFoundException;
import com.edu.pwc.forum.persistence.entity.UserEntity;
import com.edu.pwc.forum.persistence.repositories.UserRepository;
import com.edu.pwc.forum.service.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        UserEntity entity = userMapper.requestToEntity(request);
        entity = userRepository.save(entity);

        return userMapper.entityToResponse(entity);
    }

    public PageResult<UserResponse> findAll(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<UserEntity> users = userRepository.findAll(pageable);
        List<UserResponse> usersDTO = users.getContent().stream().map(userMapper::entityToResponse).toList();

        PageResult<UserResponse> pageResult = new PageResult<>();
        pageResult.setContent(usersDTO);
        pageResult.setPage(users.getNumber());
        pageResult.setSize(users.getSize());
        pageResult.setTotalElements(users.getTotalElements());
        pageResult.setTotalPages(users.getTotalPages());
        pageResult.setEmpty(users.isEmpty());
        return pageResult;
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("User with username %s was not found", username)));
    }
}
