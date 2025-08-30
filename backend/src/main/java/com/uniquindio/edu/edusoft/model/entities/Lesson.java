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
        name = "lessons",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title", "module_id"})
        }
)
public class Lesson extends BaseEntity {

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private int orderNumber;
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    private EnumLifecycleStatus status = EnumLifecycleStatus.BORRADOR;

    private boolean visible = true;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
}
