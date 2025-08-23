package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.mapper.UserMapper;
import com.uniquindio.edu.edusoft.model.dto.user.CreateUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.VerifyAccountEmailCodeDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.UserService;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> createUser(CreateUserDTO createUserDTO) throws Exception {
        // Verificar duplicados por email o documento
        Optional<User> existingUser = userRepository.findByEmailOrDocumentNumber(
                createUserDTO.email(), createUserDTO.documentNumber()
        );

        if (existingUser.isPresent()) {
            String message = existingUser.get().getEmail().equals(createUserDTO.email())
                    ? String.format("El email %s ya está registrado", createUserDTO.email())
                    : String.format("La cédula %s ya está registrada", createUserDTO.documentNumber());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(409, message, null));
        }

        // Mapeo DTO -> entidad User
        User user = userMapper.toDocumentCreate(createUserDTO);

        // Asignación de datos predeterminados
        user.setCreatedAt(LocalDate.now());
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));
        user.setVerification(false);

        // Determinar tipo de usuario según el dominio del correo
        String email = createUserDTO.email().toLowerCase();
        if (email.endsWith("@uqvirtual.edu.co")) {
            user.setUserType(EnumUserType.ESTUDIANTE);
        } else if (email.endsWith("@uniquindio.edu.co")) {
            user.setUserType(EnumUserType.PROFESOR);
        } else {
            user.setUserType(EnumUserType.AUDITOR);
        }

        // Guardar en PostgreSQL
        User savedUser = userRepository.save(user);

        // Construir respuesta
        ResponseUserDTO responseUserDto = userMapper.toDtoResponseUser(savedUser);

        emailService.SendMailHome(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Usuario creado exitosamente", responseUserDto));
    }

    @Override
    public ResponseEntity<?> sendCodeConfirmation(String email) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<?> verifyAccountEmailCode(VerifyAccountEmailCodeDTO verifyAccountEmailCodeDto) throws Exception {
        return null;
    }
}
