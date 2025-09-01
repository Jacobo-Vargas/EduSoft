package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.category.CategoryRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;
import com.uniquindio.edu.edusoft.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        CategoryResponseDto result = categoryService.createCategory(dto);

        if (result == null) {
            // 400 con un JSON { "message": "..." }
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Ya existe una categoría con ese nombre"));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}