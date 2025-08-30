package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "modules",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title", "course_id"})
        }
)
public class Module extends BaseEntity {

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 800)
    private String description;

    private int orderNumber;

    @Enumerated(EnumType.STRING)
    private EnumLifecycleStatus status = EnumLifecycleStatus.BORRADOR;

    private boolean visible = true;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
}
