package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumFileType;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "contents",
        indexes = {
                @Index(name = "idx_content_course", columnList = "course_id"),
                @Index(name = "idx_content_lesson", columnList = "lesson_id"),
                @Index(name = "idx_content_order", columnList = "lesson_id, display_order")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_content_order_lesson", columnNames = {"display_order", "lesson_id"})
        }
)
public class Content extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 1000)
    @Column(name = "file_url", length = 1000, nullable = false)
    private String fileUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Relación opcional con lección (contenido puede estar sin asignar)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    // Orden dentro de la lección (null si no está asignado)
    @Column(name = "display_order")
    private Integer displayOrder;

    @NotNull
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = false;

    @Size(max = 10)
    @Column(name = "file_extension", length = 10)
    private String fileExtension;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 20)
    private EnumFileType fileType = EnumFileType.UNKNOWN;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lifecycle_status", nullable = false)
    private EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.BORRADOR;

}