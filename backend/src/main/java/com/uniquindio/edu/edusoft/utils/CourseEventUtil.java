package com.uniquindio.edu.edusoft.utils;

import com.uniquindio.edu.edusoft.model.entities.*;
import com.uniquindio.edu.edusoft.model.entities.Module;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseEventType;
import com.uniquindio.edu.edusoft.repository.CourseEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseEventUtil {

    private final CourseEventRepository courseEventRepository;

    public void registerEvent(
            Course course,
            Module module,
            Lesson lesson,
            Content content,
            User user,
            EnumCourseEventType type,
            String description
    ) {
        CourseEvent event = new CourseEvent();
        event.setCourse(course);
        event.setModule(module);
        event.setLesson(lesson);
        event.setContent(content);
        event.setUser(user);
        event.setEventType(type);
        event.setDescription(description);
        courseEventRepository.save(event);
    }

}
