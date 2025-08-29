package com.uniquindio.edu.edusoft.model.dto.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModuleDTO {

    private Long id;
    private String title;
    private String description;
    private int orderNumber;

    private Long courseId;
    private String courseTitle;
}
