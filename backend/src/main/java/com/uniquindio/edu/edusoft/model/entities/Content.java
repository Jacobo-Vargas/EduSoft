package com.uniquindio.edu.edusoft.model.entities;

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

    @NotBlank
    @Size(max = 1000)
    @Column(name = "file_url", length = 1000, nullable = false)
    private String fileUrl;

    @NotBlank
    @Size(max = 50)
    @Column(name = "content_type", length = 50, nullable = false)
    private String contentType; // doc, png, mp4, pdf

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
    private Boolean isVisible = true;

    @Column(name = "file_size")
    private Long fileSize;

    @Size(max = 10)
    @Column(name = "file_extension", length = 10)
    private String fileExtension;
}