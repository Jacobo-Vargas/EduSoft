package com.uniquindio.edu.edusoft.model.DTO.user.respose;

import com.uniquindio.edu.edusoft.model.enums.EnumUserType;

import java.time.LocalDate;

public record ResponseUserDto(

        String documentNumber,
        String name,
        String email,
        String phone,
        String address,
        String password,
        EnumUserType userType,
        LocalDate createdAt

) {
}
