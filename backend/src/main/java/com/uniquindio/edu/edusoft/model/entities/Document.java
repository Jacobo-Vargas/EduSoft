package com.uniquindio.edu.edusoft.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "documents")
public class Document extends BaseEntity {
    private String name;
    private String url;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
