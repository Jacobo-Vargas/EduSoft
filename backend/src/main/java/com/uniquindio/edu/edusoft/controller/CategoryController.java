package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.category.CategoryRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;
import com.uniquindio.edu.edusoft.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        CategoryResponseDto result = categoryService.createCategory(dto);
        if (result == null) {
            // 400 con un JSON { "message": "..." }
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Ya existe un estado  con ese nombre"));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}