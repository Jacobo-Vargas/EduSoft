package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    Optional<UserCourse> findByUserIdAndCourseId(Long userId, Long courseId);
}
