package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.content.ContentRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentResponseDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import com.uniquindio.edu.edusoft.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentResponseDto> createContent(@RequestBody @Valid ContentRequestDto dto, Authentication authentication) throws Exception {
        String userEmail = authentication.getName();
        return contentService.createContent(dto, userEmail);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ContentResponseDto>> getContentsByCourse(@PathVariable Long courseId) throws Exception {
        return contentService.getContentsByCourse(courseId);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<ContentResponseDto>> getContentsByLesson(@PathVariable Long lessonId) throws Exception {
        return contentService.getContentsByLesson(lessonId);
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignContentsToLesson(
            @RequestBody @Valid ContentAssignmentDto dto,
            Authentication authentication) throws Exception {
        String userEmail = authentication.getName();
        return contentService.assignContentsToLesson(dto, userEmail);
    }

    @PutMapping("/{contentId}/unassign")
    public ResponseEntity<?> unassignContent(
            @PathVariable Long contentId,
            Authentication authentication) throws Exception {
        String userEmail = authentication.getName();
        return contentService.unassignContent(contentId, userEmail);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deleteContent(
            @PathVariable Long contentId,
            Authentication authentication) throws Exception {
        String userEmail = authentication.getName();
        return contentService.deleteContent(contentId, userEmail);
    }
}
