package com.edu.pwc.forum.service.mappers;

import com.edu.pwc.forum.api.dtos.CategoryResponse;
import com.edu.pwc.forum.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toDto(CategoryEntity entity);
}
