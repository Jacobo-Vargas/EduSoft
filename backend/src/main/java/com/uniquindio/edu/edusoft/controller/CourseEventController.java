package com.uniquindio.edu.edusoft.controller;


import com.uniquindio.edu.edusoft.model.dto.history.CourseEventResponseDto;
import com.uniquindio.edu.edusoft.model.entities.CourseEvent;
import com.uniquindio.edu.edusoft.model.mapper.CourseEventMapper;
import com.uniquindio.edu.edusoft.repository.CourseEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course-event")
@RequiredArgsConstructor
public class CourseEventController {

    private final CourseEventRepository courseEventRepository;
    private final CourseEventMapper eventMapper;

    @GetMapping("/courses/{courseId}/history")
    public ResponseEntity<List<CourseEventResponseDto>> getCourseHistory(@PathVariable Long courseId) {
        List<CourseEvent> events = courseEventRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
        return ResponseEntity.ok(eventMapper.toResponseDtoList(events));
    }

}

