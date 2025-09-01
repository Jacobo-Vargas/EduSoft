package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.category.CategoryRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Category;
import com.uniquindio.edu.edusoft.model.mapper.CategoryMapper;
import com.uniquindio.edu.edusoft.repository.CategoryRepository;
import com.uniquindio.edu.edusoft.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponseDto createCategory(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            return null;
        }
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDto(saved);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryMapper.toResponseDtoList(categoryRepository.findAll());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return categoryMapper.toResponseDto(category);
    }
}

