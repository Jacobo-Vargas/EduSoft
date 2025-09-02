package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.Module;
import com.uniquindio.edu.edusoft.model.entities.Lesson;
import com.uniquindio.edu.edusoft.model.entities.User;

public interface ValidationService {

    User validateTeacher(String userId) throws Exception;

    Course validateCourseOwnership(Long courseId, String userId) throws Exception;

    Module validateModuleOwnership(Long moduleId, String userId) throws Exception;

    Lesson validateLessonOwnership(Long lessonId, String userId) throws Exception;

    void validatePublicationRules(Module module) throws Exception;

    void validatePublicationRules(Lesson lesson) throws Exception;
}