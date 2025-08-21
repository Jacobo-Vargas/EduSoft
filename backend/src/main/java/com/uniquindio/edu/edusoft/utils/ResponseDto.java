package com.uniquindio.edu.edusoft.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseDto {

    private int codigo;

    private String mensaje;

    private Object datos;

}
