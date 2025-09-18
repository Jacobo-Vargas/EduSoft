package com.uniquindio.edu.edusoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.edusoft.controller.CourseController;
import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.dto.course.CourseResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.uniquindio.edu.edusoft.config.security.JwtAuthFilter.class)
        })
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testCreateCourse() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Curso de Prueba");
        responseDto.setDescription("Descripción del curso");

        CourseRequestDto dto = new CourseRequestDto();
        dto.setTitle("Curso de Prueba");
        dto.setDescription("Descripción del curso");
        dto.setPrice(BigDecimal.valueOf(100));
        dto.setSemester(1);
        dto.setPriorKnowledge("Conocimientos básicos");
        dto.setEstimatedDurationMinutes(120);
        dto.setCategoryId(1L);
        dto.setUserId("user123");

        when(courseService.createCourse(any(CourseRequestDto.class),""))
                .thenReturn((ResponseEntity) ResponseEntity.ok(responseDto));

        MockMultipartFile cover = new MockMultipartFile(
                "coverUrl",
                "cover.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart("/api/course/save")
                        .file(cover)
                        .param("title", dto.getTitle())
                        .param("description", dto.getDescription())
                        .param("price", dto.getPrice().toString())
                        .param("semester", dto.getSemester().toString())
                        .param("priorKnowledge", dto.getPriorKnowledge())
                        .param("estimatedDurationMinutes", dto.getEstimatedDurationMinutes().toString())
                        .param("categoryId", dto.getCategoryId().toString())
                        .param("userId", dto.getUserId())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value(dto.getTitle()));


    }

    @Test
    void testGetCoursesByUser() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Curso 1");

        when(courseService.getCoursesByUser(anyLong()))
                .thenReturn((ResponseEntity) ResponseEntity.ok(List.of(responseDto)));

        mockMvc.perform(get("/api/course/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Curso 1"));
    }

    @Test
    void testUpdateCourse() throws Exception {
        CourseRequestDto dto = new CourseRequestDto();
        dto.setTitle("Curso Actualizado");
        dto.setDescription("Nueva descripción");

        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle(dto.getTitle());
        responseDto.setDescription(dto.getDescription());

        when(courseService.updateCourse(anyLong(), any(CourseRequestDto.class), ""))
                .thenReturn((ResponseEntity) ResponseEntity.ok(responseDto));

        MockMultipartFile cover = new MockMultipartFile(
                "coverUrl",
                "cover.png",
                "image/png",
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart("/api/course/update/{courseId}", 1L)
                        .file(cover)
                        .param("title", dto.getTitle())
                        .param("description", dto.getDescription())
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Curso Actualizado"));
    }

    @Test
    void testDeleteCourse() throws Exception {
        when(courseService.deleteCourse(anyLong()))
                .thenReturn((ResponseEntity) ResponseEntity.ok("Curso eliminado correctamente"));

        mockMvc.perform(delete("/api/course/delete/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Curso eliminado correctamente"));
    }

    @Test
    void testSearchCourses() throws Exception {
        Course course = new Course();
        course.setTitle("Curso de prueba");

        when(courseService.searchCourses("prueba"))
                .thenReturn(List.of(course));

        mockMvc.perform(get("/api/course/search")
                        .param("title", "prueba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Curso de prueba"));
    }

    @Test
    void testUpdateStatusAudit() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Curso Auditoría");

        when(courseService.updateStatusAudit(anyLong()))
                .thenReturn((ResponseEntity) ResponseEntity.ok(responseDto));

        mockMvc.perform(put("/api/course/updateCourseAuditStatus/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Curso Auditoría"));
    }

    @Test
    void testSearchCoursesById() throws Exception {
        CourseResponseDto responseDto = new CourseResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Curso ID");

        when(courseService.searchCoursesByid(anyLong()))
                .thenReturn((ResponseEntity) ResponseEntity.ok(responseDto));

        mockMvc.perform(get("/api/course/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.title").value("Curso ID"))
                .andExpect(jsonPath("$.body.id").value(1L));
    }

}
