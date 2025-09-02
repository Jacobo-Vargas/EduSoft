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
        name = "modules",
        indexes = {
                @Index(name = "idx_module_course", columnList = "course_id"),
                @Index(name = "idx_module_order", columnList = "course_id, display_order"),
                @Index(name = "idx_module_status", columnList = "lifecycle_status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_module_name_course", columnNames = {"name", "course_id"}),
                @UniqueConstraint(name = "uk_module_order_course", columnNames = {"display_order", "course_id"})
        }
)
public class Module extends BaseEntity {

    @NotBlank(message = "El nombre del módulo es obligatorio")
    @Size(max = 120, message = "El nombre del módulo no puede exceder 120 caracteres")
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Size(max = 800, message = "La descripción no puede exceder 800 caracteres")
    @Column(name = "description", length = 800)
    private String description;

    @NotNull(message = "El módulo debe pertenecer a un curso")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "El orden de visualización es obligatorio")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lifecycle_status", nullable = false)
    private EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.BORRADOR;

    @NotNull
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = true;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;
}