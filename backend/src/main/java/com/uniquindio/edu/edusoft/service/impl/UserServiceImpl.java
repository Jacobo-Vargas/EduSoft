package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.Category;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.model.mapper.UserMapper;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.mapstruct.ap.shaded.freemarker.core.ReturnInstruction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<ResponseDTO> createUser(RequestUserDTO requestUserDTO) throws Exception {
        Optional<User> existingUser = userRepository.findByEmailOrDocumentNumber(
                requestUserDTO.getEmail(),
                requestUserDTO.getDocumentNumber()
        );

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(409, "El email o la cédula ya están registrados", null));
        }

        User user = userMapper.toEntity(requestUserDTO);
        user.setCreatedAt(new Date());
        user.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
        user.setVerification(false);
        user.setVerificationToken(UUID.randomUUID().toString());

        String email = requestUserDTO.getEmail().toLowerCase();
        if (email.endsWith("@uqvirtual.edu.co")) {
            user.setUserType(EnumUserType.ESTUDIANTE);
        } else if (email.endsWith("@uniquindio.edu.co") || email.endsWith("@gmail.com")) {
            user.setUserType(EnumUserType.PROFESOR);
        } else if (email.endsWith("@auditor.edu.co")) {
            user.setUserType(EnumUserType.AUDITOR);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(400, "El dominio del correo no es válido", null));
        }

        User savedUser = userRepository.save(user);
        ResponseUserDTO responseUserDto = userMapper.toResponseDTO(savedUser);

        emailService.SendMailHome(user.getEmail(), user.getVerificationToken());

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

    @Override
    public ResponseEntity<ResponseDTO> verifyUserByToken(String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(400, "Token inválido o expirado", null));
        }

        User user = userOpt.get();

        if (user.isVerification()) {
            return ResponseEntity.ok(new ResponseDTO(200, "La cuenta ya había sido verificada anteriormente", null));
        }

        user.setVerification(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        try {
            emailService.sendVerificationConfirmation(user.getEmail());
        } catch (Exception e) {
            System.err.println("Error enviando correo de confirmación: " + e.getMessage());
        }

        return ResponseEntity.ok(new ResponseDTO(200, "¡Cuenta verificada exitosamente! Revisa tu correo.", null));
    }

    @Override
    public ResponseEntity<?> userInformation(Authentication authentication) throws Exception {
        User user = userRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        ResponseUserDTO reponseUserDTO = userMapper.toSafeResponseDTO(user);
        return ResponseEntity.ok(new ResponseDTO(200, "Usuario encontrado", reponseUserDTO));
    }

    @Override
    public ResponseEntity<?> userUpdateInformation(Authentication authentication, RequestUserDTO requestUserDTO   ) throws Exception {

        String errors = validateFilds(requestUserDTO);

        if(!errors.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(400, errors, null));
        }
        User user = userRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setName(requestUserDTO.getName());
        user.setPhone(requestUserDTO.getPhone());
        user.setEmail(requestUserDTO.getEmail());
        user.setAddress(requestUserDTO.getAddress());
        user.setSemester(requestUserDTO.getSemester());

        if (requestUserDTO.getCoverUrl() != null && !requestUserDTO.getCoverUrl().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadFile(requestUserDTO.getCoverUrl() );
            String imageUrl = (String) uploadResult.get("secure_url");
            user.setCoverUrl(imageUrl);
        }

        userRepository.save(user);
        return ResponseEntity.ok(new ResponseDTO(200, "Usuario encontrado", null));
    }

    public String validateFilds(RequestUserDTO requestUserDTO) {
        StringBuilder message = new StringBuilder();
        // Validar título
        if (requestUserDTO.getName() == null || requestUserDTO.getName().isBlank()) {
            message.append("El nombre no puede estar vacío. ");
        }
        // Validar descripción
        if (requestUserDTO.getAddress() == null || requestUserDTO.getAddress().isBlank()) {
            message.append("La direccion obligatoria. ");
        }
        // Validar categoría
        if (requestUserDTO.getPhone() == null || requestUserDTO.getPhone().isBlank()) {
            message.append("El cerlular no puede estar vacio. ");
        }else if (!requestUserDTO.getPhone().matches("^\\d{10}$")) {
            message.append("El celular debe tener exactamente 10 dígitos numéricos. ");
        }
        // Validar título
        if (requestUserDTO.getEmail() == null || requestUserDTO.getEmail().isBlank()) {
            message.append("El email no puede estar vacío. ");
        }
        if(requestUserDTO.getSemester() == null  ){
            message.append("El semestre no puede estar vacio");
        }
        return message.toString().trim();
    }



}
