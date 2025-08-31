package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.dto.course.RequestCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.module.RequestModuleDTO;
import com.uniquindio.edu.edusoft.model.dto.lesson.RequestLessonDTO;
import com.uniquindio.edu.edusoft.model.dto.document.RequestDocumentDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import org.springframework.http.ResponseEntity;

public interface CourseService {

    // CRUD curso
    ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto) throws Exception;
//    ResponseEntity<ResponseDTO> updateCourse(Integer id, RequestCourseDTO requestCourseDTO) throws Exception;
//    ResponseEntity<ResponseDTO> getCourseById(Integer id) throws Exception;
//    ResponseEntity<ResponseDTO> getAllCourses() throws Exception;
//    ResponseEntity<ResponseDTO> deleteCourse(Integer id) throws Exception;
//
//    // Manejo de la jerarquía
//    ResponseEntity<ResponseDTO> addModuleToCourse(Integer courseId, RequestModuleDTO requestModuleDTO) throws Exception;
//    ResponseEntity<ResponseDTO> addLessonToModule(Integer moduleId, RequestLessonDTO requestLessonDTO) throws Exception;
//    ResponseEntity<ResponseDTO> addDocumentToLesson(Integer lessonId, RequestDocumentDTO requestDocumentDTO) throws Exception;
}
