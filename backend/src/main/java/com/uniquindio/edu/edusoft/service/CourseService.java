package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import org.springframework.http.ResponseEntity;

public interface CourseService {

    // CRUD curso
    ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto) throws Exception;
    ResponseEntity<?> getCoursesByUser(Long userId) throws Exception;
}
