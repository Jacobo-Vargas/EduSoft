package com.uniquindio.edu.edusoft.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uniquindio.edu.edusoft.model.enums.EnumState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(name = "idx_course_title", columnList = "title"),
                @Index(name = "idx_course_category", columnList = "category_id"),
                @Index(name = "idx_course_user", columnList = "user_id"),
                @Index(name = "idx_course_audit_status", columnList = "audit_status_id")
        })
public class Course extends BaseEntity {

    @NotBlank
    @Size(max = 120)
    @Column(name = "title", length = 120, nullable = false)
    private String title;

    @NotBlank
    @Size(max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @NotNull
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Size(max = 1000)
    @Column(name = "cover_url", length = 1000, nullable = false)
    private String coverUrl;//Url de la portada

    @NotNull
    @Column(name = "semester", nullable = false)
    private int semester; //Semestre

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_status_id", nullable = false)
    @JsonIgnore
    private CurrentStatus currentStatus; //Estado del curso

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Size(max = 500)
    @Column(name = "prior_knowledge", length = 500)
    private String priorKnowledge; //saberes previos

    @NotNull
    @Column(name = "estimated_duration_minutes", nullable = false)
    private int estimatedDurationMinutes;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "audit_status_id", nullable = false)
    @JsonIgnore
    private AuditStatus auditStatus;//Estados de auditoria

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "course_state", nullable = false, length = 20)
    private EnumState state = EnumState.ACTIVE;

    @NotNull
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = false;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Module> modules;


}
