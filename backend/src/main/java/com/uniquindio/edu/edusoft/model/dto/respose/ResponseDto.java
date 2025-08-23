package com.uniquindio.edu.edusoft.model.dto.respose;

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
