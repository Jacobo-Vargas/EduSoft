package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.service.CourseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping(value = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCourse(@ModelAttribute CourseRequestDto courseRequestDto) throws Exception {
        return courseService.createCourse(courseRequestDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCoursesByUser(@PathVariable Long userId) throws Exception {
        return courseService.getCoursesByUser(userId);
    }

    @PutMapping(value = "/update/{courseId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCourse(
            @PathVariable Long courseId,
            @ModelAttribute CourseRequestDto courseRequestDto) throws Exception {
        return courseService.updateCourse(courseId, courseRequestDto);
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) throws Exception {
        return courseService.deleteCourse(courseId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String title) throws Exception {
        return ResponseEntity.ok(courseService.searchCourses(title));
    }

    @PutMapping("/updateCourseAuditStatus/{courseId}")
    public ResponseEntity<?> updateCourseStatusAudit(@PathVariable Long courseId) throws Exception {
        return this.courseService.updateStatusAudit(courseId);
    }



}
