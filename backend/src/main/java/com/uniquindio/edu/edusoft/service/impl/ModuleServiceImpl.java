package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.module.ModuleRequestDto;
import com.uniquindio.edu.edusoft.model.dto.module.ModuleResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.Module;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseEventType;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.model.mapper.ModuleMapper;
import com.uniquindio.edu.edusoft.repository.CourseRepository;
import com.uniquindio.edu.edusoft.repository.ModuleRepository;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.ModuleService;
import com.uniquindio.edu.edusoft.utils.CourseEventUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ModuleMapper moduleMapper;
    private final CourseEventUtil courseEventUtil;

    @Override
    @Transactional
    public ResponseEntity<ModuleResponseDto> createModule(ModuleRequestDto moduleRequestDto, String userId) throws Exception {
        // Verificar que el usuario sea PROFESOR
        User user = validateTeacher(userId);

        // Verificar que el curso existe y pertenece al profesor
        Course course = validateCourseOwnership(moduleRequestDto.getCourseId(), userId);

        // Validar que no exista un módulo con el mismo nombre en el curso
        Optional<Module> existingModule = moduleRepository.findByCourseIdAndNameIgnoreCase(
                moduleRequestDto.getCourseId(), moduleRequestDto.getName());
        if (existingModule.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Si no se especifica orden, asignar el siguiente disponible
        if (moduleRequestDto.getDisplayOrder() == null) {
            moduleRequestDto.setDisplayOrder(moduleRepository.getNextDisplayOrder(moduleRequestDto.getCourseId()));
        } else {
            // Verificar que el orden no esté ocupado
            Optional<Module> existingOrder = moduleRepository.findByCourseIdAndDisplayOrder(
                    moduleRequestDto.getCourseId(), moduleRequestDto.getDisplayOrder());
            if (existingOrder.isPresent()) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        Module module = moduleMapper.toEntity(moduleRequestDto);
        module.setCourse(course);

        Module savedModule = moduleRepository.save(module);
        courseEventUtil.registerEvent(
                course,
                savedModule,
                null,
                null,
                user,
                EnumCourseEventType.MODULE_CREATED,
                "Se creó el módulo: " + savedModule.getName()
        );
        return ResponseEntity.ok(moduleMapper.toResponseDto(savedModule));
    }

    @Override
    @Transactional
    public ResponseEntity<ModuleResponseDto> updateModule(Long moduleId, ModuleRequestDto moduleRequestDto, String userId) throws Exception {
        User user = validateTeacher(userId);
        Module module = validateModuleOwnership(moduleId, userId);

        // Validar nombre único en el curso (excepto el actual)
        Optional<Module> existingModule = moduleRepository.findByCourseIdAndNameIgnoreCase(
                module.getCourse().getId(), moduleRequestDto.getName());
        if (existingModule.isPresent() && !existingModule.get().getId().equals(moduleId)) {
            return ResponseEntity.badRequest().body(null);
        }

        // Actualizar campos
        module.setName(moduleRequestDto.getName());
        module.setDescription(moduleRequestDto.getDescription());

        if (moduleRequestDto.getLifecycleStatus() != null) {
            module.setLifecycleStatus(moduleRequestDto.getLifecycleStatus());
        }

        if (moduleRequestDto.getIsVisible() != null) {
            module.setIsVisible(moduleRequestDto.getIsVisible());
        }

        Module savedModule = moduleRepository.save(module);
        courseEventUtil.registerEvent(
                module.getCourse(),
                savedModule,
                null,
                null,
                user,
                EnumCourseEventType.MODULE_UPDATED,
                "Se actualizó el módulo: " + savedModule.getName()
        );

        return ResponseEntity.ok(moduleMapper.toResponseDto(savedModule));

    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteModule(Long moduleId, String userId) throws Exception {
        User user = validateTeacher(userId);
        Module module = validateModuleOwnership(moduleId, userId);

        module.setLifecycleStatus(EnumLifecycleStatus.ELIMINADO);
        module.setDeletedAt(new Date());
        module.setUpdatedAt(new Date());

        moduleRepository.save(module);

        courseEventUtil.registerEvent(
                module.getCourse(),
                module,
                null,
                null,
                user,
                EnumCourseEventType.MODULE_DELETED,
                "Se eliminó el módulo: " + module.getName()
        );
        return ResponseEntity.ok("Módulo eliminado correctamente");
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<ModuleResponseDto>> getModulesByCourse(Long courseId) throws Exception {
        List<Module> modules = moduleRepository.findByCourseIdWithCourse(courseId);
        return ResponseEntity.ok(moduleMapper.toResponseDtoList(modules));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ModuleResponseDto> getModuleById(Long moduleId) throws Exception {
        Module module = moduleRepository.findByIdWithCourse(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado"));
        return ResponseEntity.ok(moduleMapper.toResponseDto(module));
    }

    @Override
    @Transactional
    public ResponseEntity<?> reorderModules(Long courseId, ReorderRequestDto reorderRequest, String userId) throws Exception {
        User user = validateTeacher(userId);
        Course course = validateCourseOwnership(courseId, userId);

        // Validar que todos los IDs existan y pertenezcan al curso
        List<Long> moduleIds = reorderRequest.getItems().stream()
                .map(ReorderRequestDto.ReorderItemDto::getId)
                .collect(Collectors.toList());

        List<Module> modules = moduleRepository.findAllById(moduleIds);
        if (modules.size() != moduleIds.size()) {
            return ResponseEntity.badRequest().body("Algunos módulos no existen");
        }

        // Verificar que todos los módulos pertenecen al curso
        boolean allBelongToCourse = modules.stream()
                .allMatch(module -> module.getCourse().getId().equals(courseId));

        if (!allBelongToCourse) {
            return ResponseEntity.badRequest().body("Algunos módulos no pertenecen al curso");
        }

        // Validar que no hay órdenes duplicados
        List<Integer> newOrders = reorderRequest.getItems().stream()
                .map(ReorderRequestDto.ReorderItemDto::getNewOrder)
                .collect(Collectors.toList());

        if (newOrders.size() != newOrders.stream().distinct().count()) {
            return ResponseEntity.badRequest().body("Órdenes duplicados no permitidos");
        }

        // Aplicar reordenamiento
        for (ReorderRequestDto.ReorderItemDto item : reorderRequest.getItems()) {
            Module module = modules.stream()
                    .filter(m -> m.getId().equals(item.getId()))
                    .findFirst()
                    .orElseThrow();

            module.setDisplayOrder(item.getNewOrder());
        }

        moduleRepository.saveAll(modules);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> toggleModuleVisibility(Long moduleId, String userId) throws Exception {
        User user = validateTeacher(userId);
        Module module = validateModuleOwnership(moduleId, userId);

        module.setIsVisible(!module.getIsVisible());
        moduleRepository.save(module);

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> changeModuleStatus(Long moduleId, String status, String userId) throws Exception {
        User user = validateTeacher(userId);
        Module module = validateModuleOwnership(moduleId, userId);

        try {
            EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.valueOf(status.toUpperCase());
            module.setLifecycleStatus(lifecycleStatus);
            moduleRepository.save(module);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido");
        }
    }

    private User validateTeacher(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getUserType() != EnumUserType.PROFESOR) {
            throw new IllegalArgumentException("Solo los profesores pueden gestionar módulos");
        }

        return user;
    }

    private Course validateCourseOwnership(Long courseId, String email) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        if (!course.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("No tiene permisos para modificar este curso");
        }

        return course;
    }

    private Module validateModuleOwnership(Long moduleId, String userEmail) throws Exception {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado"));

        if (!module.getCourse().getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("No tiene permisos para modificar este módulo");
        }

        return module;
    }
}