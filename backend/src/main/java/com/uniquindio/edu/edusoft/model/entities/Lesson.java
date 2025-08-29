package com.uniquindio.edu.edusoft.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity {

    private String title;

    @Column(length = 2000)
    private String content;

    private int orderNumber;
    private int durationMinutes;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;
}
