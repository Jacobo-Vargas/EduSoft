package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.document.RequestDocumentDTO;
import com.uniquindio.edu.edusoft.model.dto.document.ResponseDocumentDTO;
import com.uniquindio.edu.edusoft.model.entities.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toEntity(RequestDocumentDTO requestDocumentDTO);

    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "lesson.title", target = "lessonTitle")
    ResponseDocumentDTO toResponseDTO(Document document);

    List<ResponseDocumentDTO> toResponseDTOList(List<Document> documents);
}
