package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumUserCourse;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "user_courses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "course_id"})
        },
        indexes = {
                @Index(name = "idx_user_course_user", columnList = "user_id"),
                @Index(name = "idx_user_course_course", columnList = "course_id"),
                @Index(name = "idx_user_course_enrollment_date", columnList = "enrollment_date")
        }
)
public class UserCourse extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "progress_percentage")
    private Double progressPercentage = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_course")
    private EnumUserCourse userCourse;


}