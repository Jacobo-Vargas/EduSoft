package com.uniquindio.edu.edusoft.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(
        name = "categories",
        indexes = {
                @Index(name = "idx_category_name", columnList = "name")
        })
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity  {
    @NotBlank
    @Size(max = 120)
    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;
}
