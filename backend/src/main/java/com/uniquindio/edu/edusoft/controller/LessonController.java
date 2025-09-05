package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.lesson.LessonRequestDto;
import com.uniquindio.edu.edusoft.model.dto.lesson.LessonResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import com.uniquindio.edu.edusoft.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LessonResponseDto> createLesson(
            @RequestBody @Valid LessonRequestDto lessonRequestDto,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.createLesson(lessonRequestDto, userId);
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LessonResponseDto> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody @Valid LessonRequestDto lessonRequestDto,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.updateLesson(lessonId, lessonRequestDto, userId);
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteLesson(
            @PathVariable Long lessonId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.deleteLesson(lessonId, userId);
    }

    @GetMapping("/module/{moduleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LessonResponseDto>> getLessonsByModule(
            @PathVariable Long moduleId) throws Exception {
        return lessonService.getLessonsByModule(moduleId);
    }

    @GetMapping("/{lessonId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LessonResponseDto> getLessonById(
            @PathVariable Long lessonId) throws Exception {
        return lessonService.getLessonById(lessonId);
    }

    @PutMapping("/module/{moduleId}/reorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> reorderLessons(
            @PathVariable Long moduleId,
            @RequestBody @Valid ReorderRequestDto reorderRequest,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.reorderLessons(moduleId, reorderRequest, userId);
    }

    @PutMapping("/{lessonId}/move")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> moveLessonToModule(
            @PathVariable Long lessonId,
            @RequestParam Long newModuleId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.moveLessonToModule(lessonId, newModuleId, userId);
    }

    @PutMapping("/{lessonId}/toggle-visibility")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> toggleVisibility(
            @PathVariable Long lessonId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.toggleLessonVisibility(lessonId, userId);
    }

    @PutMapping("/{lessonId}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long lessonId,
            @RequestParam String status,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.changeLessonStatus(lessonId, status, userId);
    }

    @PostMapping("/assign-contents")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> assignContentsToLesson(
            @RequestBody @Valid ContentAssignmentDto assignmentDto,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.assignContentsToLesson(assignmentDto, userId);
    }

    @PutMapping("/contents/{contentId}/unassign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> unassignContentFromLesson(
            @PathVariable Long contentId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.unassignContentFromLesson(contentId, userId);
    }

    @PutMapping("/{lessonId}/contents/reorder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> reorderContentsInLesson(
            @PathVariable Long lessonId,
            @RequestBody @Valid ReorderRequestDto reorderRequest,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.reorderContentsInLesson(lessonId, reorderRequest, userId);
    }
}