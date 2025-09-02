package com.uniquindio.edu.edusoft.model.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CodeChangePassword extends BaseEntity {
    private String code;
    @ManyToOne(fetch = FetchType.LAZY) // Relación muchos a uno
    @JoinColumn(name = "user_id", nullable = false) // Definir la columna que hace la relación con User
    private User user;
    private Long expirationTime;
}
