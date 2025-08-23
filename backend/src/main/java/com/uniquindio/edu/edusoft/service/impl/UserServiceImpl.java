package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.model.mapper.UserMapper;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.UserService;
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
    public ResponseEntity<ResponseDTO> createUser(RequestUserDTO requestUserDTO) throws Exception {
        // Verificar duplicados por email o documento
        Optional<User> existingUser = userRepository.findByEmailOrDocumentNumber(
                requestUserDTO.getEmail(), requestUserDTO.getDocumentNumber()
        );

        if (existingUser.isPresent()) {
            String message = existingUser.get().getEmail().equals(requestUserDTO.getEmail())
                    ? String.format("El email %s ya está registrado", requestUserDTO.getEmail())
                    : String.format("La cédula %s ya está registrada", requestUserDTO.getDocumentNumber());

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(409, "La cédula o el email ya están registrados", null));
        }

        // Mapeo DTO -> entidad User
        User user = userMapper.toEntity(requestUserDTO);

        // Asignación de datos predeterminados
        user.setCreatedAt(LocalDate.now());
        user.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
        user.setVerification(false);

        // Determinar tipo de usuario según el dominio del correo
        String email = requestUserDTO.getEmail().toLowerCase();

        if (email.endsWith("@uqvirtual.edu.co")) {
            user.setUserType(EnumUserType.ESTUDIANTE);
        } else if (email.endsWith("@uniquindio.edu.co")) {
            user.setUserType(EnumUserType.PROFESOR);
        } else if (email.endsWith("@auditor.edu.co")) {
            user.setUserType(EnumUserType.AUDITOR);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(400, "El dominio del correo no es válido", null));
        }

        // Guardar en PostgreSQL
        User savedUser = userRepository.save(user);

        // Construir respuesta
        ResponseUserDTO responseUserDto = userMapper.toResponseDTO(savedUser);

        // Enviar correo de bienvenida
        emailService.SendMailHome(user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(201, "Usuario creado exitosamente", responseUserDto));
    }

    @Override
    public ResponseEntity<ResponseDTO> sendCodeConfirmation(String email) throws Exception {
        return ResponseEntity.ok(new ResponseDTO(200, "Código enviado al correo " + email, null));
    }

    @Override
    public ResponseEntity<ResponseDTO> verifyAccountEmailCode(String email, String code) throws Exception {

        return ResponseEntity.ok(new ResponseDTO(200, "Cuenta verificada correctamente", null));
    }
}
