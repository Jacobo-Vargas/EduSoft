package com.uniquindio.edu.edusoft.model.entities;

import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("ALL")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    private String address;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private EnumUserType userType;

    @Column(name = "verification")
    private boolean verification;

    @Column(name = "verification_token")
    private String verificationToken;


    public boolean isVerification() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }
}
