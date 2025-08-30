package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.course.RequestCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.course.ResponseCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.document.RequestDocumentDTO;
import com.uniquindio.edu.edusoft.model.dto.lesson.RequestLessonDTO;
import com.uniquindio.edu.edusoft.model.dto.module.RequestModuleDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.Lesson;
import com.uniquindio.edu.edusoft.model.mapper.CourseMapper;
import com.uniquindio.edu.edusoft.model.mapper.LessonMapper;
import com.uniquindio.edu.edusoft.model.mapper.ModuleMapper;
import com.uniquindio.edu.edusoft.repository.CourseRepository;
import com.uniquindio.edu.edusoft.repository.LessonRepository;
import com.uniquindio.edu.edusoft.repository.ModuleRepository;
import com.uniquindio.edu.edusoft.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl {

    /*private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final ModuleMapper moduleMapper;
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public ResponseEntity<ResponseDTO> createCourse(RequestCourseDTO requestCourseDTO) throws Exception {
        Course course = courseMapper.toEntity(requestCourseDTO);
        course.setCreatedAt(new Date());
        Course savedCourse = courseRepository.save(course);
        ResponseCourseDTO response = courseMapper.toResponseDTO(savedCourse);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Curso creado exitosamente", response));
    }

    @Override
    public ResponseEntity<ResponseDTO> updateCourse(Integer id, RequestCourseDTO requestCourseDTO) throws Exception {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "Curso no encontrado", null));
        }

        Course course = courseOpt.get();
        course.setTitle(requestCourseDTO.getTitle());
        course.setDescription(requestCourseDTO.getDescription());
        course.setUpdatedAt(new Date());

        Course updated = courseRepository.save(course);
        ResponseCourseDTO response = courseMapper.toResponseDTO(updated);

        return ResponseEntity.ok(new ResponseDTO(200, "Curso actualizado exitosamente", response));
    }

    @Override
    public ResponseEntity<ResponseDTO> getCourseById(Integer id) throws Exception {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "Curso no encontrado", null));
        }

        ResponseCourseDTO response = courseMapper.toResponseDTO(courseOpt.get());
        return ResponseEntity.ok(new ResponseDTO(200, "Curso encontrado", response));
    }

    @Override
    public ResponseEntity<ResponseDTO> getAllCourses() throws Exception {
        List<Course> courses = courseRepository.findAll();
        List<ResponseCourseDTO> response = courseMapper.toResponseDTOList(courses);

        return ResponseEntity.ok(new ResponseDTO(200, "Lista de cursos obtenida", response));
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteCourse(Integer id) throws Exception {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "Curso no encontrado", null));
        }

        courseRepository.deleteById(id);
        return ResponseEntity.ok(new ResponseDTO(200, "Curso eliminado exitosamente", null));
    }
    @Override
    public ResponseEntity<ResponseDTO> addModuleToCourse(Integer courseId, RequestModuleDTO requestModuleDTO) throws Exception {
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "Curso no encontrado", null));
        }

        Course course = courseOpt.get();
        Module module = moduleMapper.toEntity(requestModuleDTO);
        module.setCourse(course);

        Module savedModule = moduleRepository.save(module);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Módulo agregado al curso", moduleMapper.toResponseDTO(savedModule)));
    }

    @Override
    public ResponseEntity<ResponseDTO> addLessonToModule(Integer moduleId, RequestLessonDTO requestLessonDTO) throws Exception {
        Optional<Module> moduleOpt = moduleRepository.findById(moduleId);

        if (moduleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "Módulo no encontrado", null));
        }

        Module module = moduleOpt.get();
        Lesson lesson = lessonMapper.toEntity(requestLessonDTO);
        lesson.setModule(module);

        Lesson savedLesson = lessonRepository.save(lesson);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Lección agregada al módulo", lessonMapper.toResponseDTO(savedLesson)));
    }

    @Override
    public ResponseEntity<ResponseDTO> addDocumentToLesson(Integer lessonId, RequestDocumentDTO requestDocumentDTO) throws Exception {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);

        if (lessonOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "Lección no encontrada", null));
        }

        Lesson lesson = lessonOpt.get();
        Document document = documentMapper.toEntity(requestDocumentDTO);
        document.setLesson(lesson);

        Document savedDocument = documentRepository.save(document);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Documento agregado a la lección", documentMapper.toResponseDTO(savedDocument)));
    }*/

}
