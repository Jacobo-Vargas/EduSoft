package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createCourse(@ModelAttribute CourseRequestDto courseRequestDto) throws Exception {
        return courseService.createCourse(courseRequestDto);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCoursesByUser(@PathVariable Long userId) throws Exception {
        return courseService.getCoursesByUser(userId);
    }

}
