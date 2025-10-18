package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.UserCourse;
import com.uniquindio.edu.edusoft.model.enums.EnumUserCourse;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    Optional<UserCourse> findByUserIdAndCourseIdAndUserCourse(Long userId, Long courseId, EnumUserCourse estado);

    //List<UserCourse> findByUserId(Long userId);
    List<UserCourse> findByUserIdAndUserCourse(Long userId, EnumUserCourse estado);


    @Modifying
    @Transactional
    @Query(value = "UPDATE user_courses SET user_course = :user_course WHERE id = :id", nativeQuery = true)
    void updateState(@Param("id") Long id, @Param("user_course") String user_course);
}
