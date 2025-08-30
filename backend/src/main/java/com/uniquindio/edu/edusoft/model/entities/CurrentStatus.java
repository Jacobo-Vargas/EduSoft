package com.uniquindio.edu.edusoft.model.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "currentStatus")
@EqualsAndHashCode(callSuper = true)
public class CurrentStatus extends BaseEntity {

    @NotBlank
    @Size(max = 120)
    @Column(name = "nome", length = 120, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;
}
