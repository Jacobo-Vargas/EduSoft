package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.dto.course.RequestCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.course.ResponseCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.document.RequestDocumentDTO;
import com.uniquindio.edu.edusoft.model.dto.lesson.RequestLessonDTO;
import com.uniquindio.edu.edusoft.model.dto.module.RequestModuleDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.entities.Category;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.Lesson;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.mapper.CourseMapper;
import com.uniquindio.edu.edusoft.model.mapper.LessonMapper;
import com.uniquindio.edu.edusoft.model.mapper.ModuleMapper;
import com.uniquindio.edu.edusoft.repository.*;
import com.uniquindio.edu.edusoft.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto) throws Exception {
        // Validar campos
        String messageValidate = validateFilds(courseRequestDto);
        if (!messageValidate.isEmpty()) {
            return ResponseEntity.badRequest().body(messageValidate);
        }
        // Mapear DTO → Entity
        Course course = courseMapper.toEntity(courseRequestDto);
        // Validar FK category
        Category category = categoryRepository.findById(courseRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        course.setCategory(category);
        // Validar FK profesor
        User profesor = userRepository.findById(courseRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
        course.setUser(profesor);
        /*
        // Estados iniciales por defecto
        if (course.getAuditStatus() == null) {
            // Busca el estado "DRAFT" en la tabla audit_status
            AuditStatus draft = new AuditStatus();
            draft.setId(1L); //  puedes usar un Enum o un repositorio para cargarlo
            course.setAuditStatus(draft);
        }
        if (course.getCurrentStatus() == null) {
            // Busca el estado "ACTIVE" o el que quieras por defecto
            CurrentStatus active = new CurrentStatus();
            active.setId(1L);
            course.setCurrentStatus(active);
        }
         */
        // Guardar curso
        Course saved = courseRepository.save(course);
        // Retornar respuesta con DTO enriquecido
        return ResponseEntity.ok(courseMapper.toResponseDto(saved));
    }



/*    private final ModuleMapper moduleMapper;
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

        Document savedDocument = documentRepository.save(document);estado auditoria

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Documento agregado a la lección", documentMapper.toResponseDTO(savedDocument)));
    }*/


    public String validateFilds(CourseRequestDto courseRequestDto){
        StringBuilder message = new StringBuilder();

        // Validar título
        if (courseRequestDto.getTitle() == null || courseRequestDto.getTitle().isBlank()) {
            message.append("El título no puede estar vacío. ");
        } else if (courseRequestDto.getTitle().length() > 120) {
            message.append("El título no puede tener más de 120 caracteres. ");
        }

        // Validar descripción
        if (courseRequestDto.getDescription() == null || courseRequestDto.getDescription().isBlank()) {
            message.append("La descripción es obligatoria. ");
        } else if (courseRequestDto.getDescription().length() > 500) {
            message.append("La descripción no puede tener más de 500 caracteres. ");
        }

        // Validar categoría
        if (courseRequestDto.getCategoryId() == null) {
            message.append("Debe seleccionar una categoría. ");
        }

        // Validar precio
        if (courseRequestDto.getPrice() == null) {
            message.append("Debe indicar el precio. ");
        } else if (courseRequestDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            message.append("El precio no puede ser negativo. ");
        }

        // Validar portada
        if (courseRequestDto.getCoverUrl() == null || courseRequestDto.getCoverUrl().isBlank()) {
            message.append("Debe seleccionar una portada. ");
        } else if (courseRequestDto.getCoverUrl().length() > 800) {
            message.append("La URL de la portada no puede tener más de 800 caracteres. ");
        }

        // Validar semestre recomendado
        if (courseRequestDto.getSemester() == null) {
            message.append("El semestre recomendado es obligatorio. ");
        } else if (courseRequestDto.getSemester() < 0 || courseRequestDto.getSemester() > 10) {
            message.append("El semestre recomendado debe estar entre 0 y 13. ");
        }

        // Validar saberes previos
        if (courseRequestDto.getSemester() != null && courseRequestDto.getSemester() > 0) {
            if (courseRequestDto.getPriorKnowledge() == null || courseRequestDto.getPriorKnowledge().isBlank()) {
                message.append("Los saberes previos son obligatorios si el semestre recomendado es mayor a 0. ");
            } else if (courseRequestDto.getPriorKnowledge().length() > 800) {
                message.append("Los saberes previos no pueden tener más de 800 caracteres. ");
            }
        }

        // Validar profesor
        if (courseRequestDto.getUserId() == null) {
            message.append("Debe indicar un profesor válido. ");
        }
        return message.toString().trim();
    }

}
