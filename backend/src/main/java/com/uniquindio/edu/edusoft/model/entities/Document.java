package com.uniquindio.edu.edusoft.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "documents")
public class Document extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private String url;

    private int orderNumber;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = true) // permite documentos sin asignar
    private Lesson lesson;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
}
