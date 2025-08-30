package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumCourseCategoty;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseLevel;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private EnumCourseCategoty category;
    private EnumCourseLevel level;
    private int estimatedDurationMinutes;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EnumLifecycleStatus status = EnumLifecycleStatus.BORRADOR;

    private boolean visible = true;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "course_enrollments",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> enrolledUsers;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
}
