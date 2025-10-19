package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {

    // CRUD curso
    ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto, String emailUser) throws Exception;
    ResponseEntity<?> getCoursesByUser(Long userId) throws Exception;
    ResponseEntity<?> updateCourse(Long courseId, CourseRequestDto courseRequestDto, String userEmail) throws Exception;
    ResponseEntity<?> deleteCourse(Long courseId) throws Exception;
    public List<Course> searchCourses(String title)throws Exception;
    ResponseEntity<?> updateStatusAudit(long courseId)throws Exception;
    ResponseEntity<?> searchCoursesByid(long id);
    ResponseEntity<?> getVisibleActiveCourses()throws Exception;

    Course getCourseById(Long id);
}
