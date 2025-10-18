package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "lessons",
        indexes = {
                @Index(name = "idx_lesson_module", columnList = "module_id"),
                @Index(name = "idx_lesson_order", columnList = "module_id, display_order"),
                @Index(name = "idx_lesson_status", columnList = "lifecycle_status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_lesson_name_module", columnNames = {"name", "module_id"}),
                @UniqueConstraint(name = "uk_lesson_order_module", columnNames = {"display_order", "module_id"})
        }
)
public class Lesson extends BaseEntity {

    @NotBlank(message = "El nombre de la lección es obligatorio")
    @Size(max = 120, message = "El nombre de la lección no puede exceder 120 caracteres")
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "La lección debe pertenecer a un módulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @NotNull(message = "El orden de visualización es obligatorio")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lifecycle_status", nullable = false)
    private EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.BORRADOR;

    @NotNull
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = false;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Content> contents;
}