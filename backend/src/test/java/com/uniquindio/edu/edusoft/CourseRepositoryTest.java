package com.uniquindio.edu.edusoft;

import com.uniquindio.edu.edusoft.model.entities.*;
import com.uniquindio.edu.edusoft.model.enums.EnumState;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuditStatusRepository auditStatusRepository;

    @Autowired
    private CurrentStatusRepository currentStatusRepository;

    private User buildUser(String document, String email) {
        User user = new User();
        user.setDocumentNumber(document);
        user.setName("Profesor Test");
        user.setEmail(email);
        user.setPassword("password");
        user.setUserType(EnumUserType.PROFESOR);
        user.setVerification(true);
        user.setPhone("3000000000");
        return userRepository.save(user);
    }

    private Category buildCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(name + " description");
        return categoryRepository.save(category);
    }

    private AuditStatus buildAuditStatus(String name) {
        AuditStatus auditStatus = new AuditStatus();
        auditStatus.setName(name);
        auditStatus.setDescription(name + " desc");
        return auditStatusRepository.save(auditStatus);
    }

    private CurrentStatus buildCurrentStatus(String name) {
        CurrentStatus currentStatus = new CurrentStatus();
        currentStatus.setName(name);
        currentStatus.setDescription(name + " desc");
        return currentStatusRepository.save(currentStatus);
    }

    private Course buildCourse(User user, Category category, String title) {
        AuditStatus auditStatus = buildAuditStatus("Audit " + title);
        CurrentStatus currentStatus = buildCurrentStatus("Current " + title);

        Course course = new Course();
        course.setTitle(title);
        course.setUser(user);
        course.setCategory(category);
        course.setState(EnumState.ACTIVE);
        course.setCoverUrl("https://example.com/cover.jpg");
        course.setDescription("Descripción de " + title);
        course.setEstimatedDurationMinutes(120);
        course.setPrice(new BigDecimal("199.99"));
        course.setSemester(1);
        course.setAuditStatus(auditStatus);
        course.setCurrentStatus(currentStatus);

        return courseRepository.save(course);
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        User user = buildUser("2001", "prof1@uq.edu.co");
        Category category = buildCategory("Matematicas");
        buildCourse(user, category, "Curso de Algebra");

        List<Course> result = courseRepository.findByTitleContainingIgnoreCase("algebra");
        assertEquals(1, result.size());
        assertEquals("Curso de Algebra", result.get(0).getTitle());
    }

    @Test
    void testExistsByTitleIgnoreCase() {
        User user = buildUser("2002", "prof2@uq.edu.co");
        Category category = buildCategory("Fisica");
        buildCourse(user, category, "Curso de Fisica");

        Boolean exists = courseRepository.existsByTitleIgnoreCase("curso de fisica");
        assertTrue(exists);

        Boolean notExists = courseRepository.existsByTitleIgnoreCase("Curso de Quimica");
        assertFalse(notExists);
    }

    @Test
    void testFindByIdWithRelations() {
        User user = buildUser("2003", "prof3@uq.edu.co");
        Category category = buildCategory("Quimica");
        Course course = buildCourse(user, category, "Curso de Quimica");

        Optional<Course> result = courseRepository.findByIdWithRelations(course.getId());
        assertTrue(result.isPresent());
        assertEquals("Curso de Quimica", result.get().getTitle());
        assertNotNull(result.get().getUser());
        assertNotNull(result.get().getCategory());
    }

    @Test
    void testFindByUserIdWithRelations() {
        User user = buildUser("2004", "prof4@uq.edu.co");
        Category category = buildCategory("Biologia");
        buildCourse(user, category, "Curso de Biologia 1");
        buildCourse(user, category, "Curso de Biologia 2");

        List<Course> result = courseRepository.findByUserIdWithRelations(user.getId());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getUser().getId().equals(user.getId())));
    }
}
