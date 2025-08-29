package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumCourseCategoty;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseLevel;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    private String title;

    @Column(length = 1000)
    private String description;

    private EnumCourseCategoty category;
    private EnumCourseLevel level;
    private int estimatedDurationMinutes;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EnumCourseType status = EnumCourseType.BORRADOR;

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
}
