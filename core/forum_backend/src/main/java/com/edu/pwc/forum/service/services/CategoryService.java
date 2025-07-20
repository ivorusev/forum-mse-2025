package com.edu.pwc.forum.service.services;

import com.edu.pwc.forum.api.dtos.CategoryResponse;
import com.edu.pwc.forum.persistence.repositories.CategoryRepository;
import com.edu.pwc.forum.service.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
