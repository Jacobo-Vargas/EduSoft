package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.content.ContentRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Content;
import com.uniquindio.edu.edusoft.model.enums.EnumFileType;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    @Mapping(source = "lessonId", target = "lesson.id")
    Content toEntity(ContentRequestDto dto);

    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "lesson.name", target = "lessonName")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseName")
    @Mapping(source = "fileType", target = "fileType")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ContentResponseDto toResponseDto(Content entity);

    List<ContentResponseDto> toResponseDtoList(List<Content> entities);

    @AfterMapping
    default void setFileType(@MappingTarget Content entity, ContentRequestDto dto) {
        String url = dto.getFileUrl();
        if (url == null) {
            entity.setFileType(EnumFileType.UNKNOWN);
            return;
        }

        if (url.contains("youtube.com/watch") || url.contains("youtu.be/")) {
            entity.setFileType(EnumFileType.YOUTUBE);
            return;
        }

        if (url.contains("vimeo.com")) {
            entity.setFileType(EnumFileType.VIMEO);
            return;
        }

        int lastDot = url.lastIndexOf('.');
        if (lastDot != -1 && lastDot < url.length() - 1) {
            String ext = url.substring(lastDot + 1).toLowerCase();
            if (ext.matches("jpg|jpeg|png|gif|bmp")) {
                entity.setFileType(EnumFileType.IMAGE);
            } else if (ext.matches("mp4|avi|mov|mkv")) {
                entity.setFileType(EnumFileType.VIDEO);
            } else if (ext.matches("mp3|wav|ogg")) {
                entity.setFileType(EnumFileType.AUDIO);
            } else if (ext.matches("pdf|docx?|pptx?|xlsx?")) {
                entity.setFileType(EnumFileType.DOCUMENT);
            } else {
                entity.setFileType(EnumFileType.UNKNOWN);
            }
        } else {
            entity.setFileType(EnumFileType.LINK);
        }
    }
}