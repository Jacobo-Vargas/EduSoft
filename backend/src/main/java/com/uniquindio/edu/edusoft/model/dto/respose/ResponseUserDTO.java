package com.uniquindio.edu.edusoft.model.dto.respose;

import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
    private String documentNumber;
    private String name;
    private String email;
    private String phone;
    private String address;
    private EnumUserType userType;
    private LocalDate createdAt;
}
