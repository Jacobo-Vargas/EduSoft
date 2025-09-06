package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.lesson.LessonRequestDto;
import com.uniquindio.edu.edusoft.model.dto.lesson.LessonResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import com.uniquindio.edu.edusoft.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponseDto> createLesson(@RequestBody @Valid LessonRequestDto lessonRequestDto, Authentication authentication) throws Exception {
        System.out.println("📥 Payload recibido: " + lessonRequestDto);
        String userId = authentication.getName();
        return lessonService.createLesson(lessonRequestDto, userId);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDto> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody @Valid LessonRequestDto lessonRequestDto,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.updateLesson(lessonId, lessonRequestDto, userId);
    }

    @DeleteMapping("/delete/{lessonId}")
    public ResponseEntity<?> deleteLesson(
            @PathVariable Long lessonId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.deleteLesson(lessonId, userId);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<LessonResponseDto>> getLessonsByModule(
            @PathVariable Long moduleId) throws Exception {
        return lessonService.getLessonsByModule(moduleId);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDto> getLessonById(
            @PathVariable Long lessonId) throws Exception {
        return lessonService.getLessonById(lessonId);
    }

    @PutMapping("/module/{moduleId}/reorder")
    public ResponseEntity<?> reorderLessons(
            @PathVariable Long moduleId,
            @RequestBody @Valid ReorderRequestDto reorderRequest,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.reorderLessons(moduleId, reorderRequest, userId);
    }

    @PutMapping("/{lessonId}/move")
    public ResponseEntity<?> moveLessonToModule(
            @PathVariable Long lessonId,
            @RequestParam Long newModuleId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.moveLessonToModule(lessonId, newModuleId, userId);
    }

    @PutMapping("/{lessonId}/toggle-visibility")
    public ResponseEntity<?> toggleVisibility(
            @PathVariable Long lessonId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.toggleLessonVisibility(lessonId, userId);
    }

    @PutMapping("/{lessonId}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long lessonId,
            @RequestParam String status,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.changeLessonStatus(lessonId, status, userId);
    }

    @PostMapping("/assign-contents")
    public ResponseEntity<?> assignContentsToLesson(
            @RequestBody @Valid ContentAssignmentDto assignmentDto,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.assignContentsToLesson(assignmentDto, userId);
    }

    @PutMapping("/contents/{contentId}/unassign")
    public ResponseEntity<?> unassignContentFromLesson(
            @PathVariable Long contentId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.unassignContentFromLesson(contentId, userId);
    }

    @PutMapping("/{lessonId}/contents/reorder")
    public ResponseEntity<?> reorderContentsInLesson(
            @PathVariable Long lessonId,
            @RequestBody @Valid ReorderRequestDto reorderRequest,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return lessonService.reorderContentsInLesson(lessonId, reorderRequest, userId);
    }
}