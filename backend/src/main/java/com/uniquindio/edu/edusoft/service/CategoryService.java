package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.category.CategoryRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    public CategoryResponseDto createCategory(CategoryRequestDto dto);
    public List<CategoryResponseDto> getAllCategories() ;
    public CategoryResponseDto getCategoryById(Long id) ;
}
