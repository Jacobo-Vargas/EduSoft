package com.uniquindio.edu.edusoft;


import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.ResponseUserDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.model.mapper.UserMapper;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.impl.EmailServiceImpl;
import com.uniquindio.edu.edusoft.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RequestUserDTO requestUserDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestUserDTO = new RequestUserDTO(
                "12345",
                "Juan Pérez",
                "3101234567",
                "Calle 123",
                "juan@uqvirtual.edu.co",
                "password123",
                EnumUserType.ESTUDIANTE,
                null,
                null
        );


        user = new User();
        user.setDocumentNumber("12345");
        user.setName("Juan Pérez");
        user.setEmail("juan@uqvirtual.edu.co");
        user.setPhone("3101234567");
        user.setPassword("encodedPassword");
        user.setUserType(EnumUserType.ESTUDIANTE);
        user.setVerification(false);
        user.setVerificationToken(UUID.randomUUID().toString());
    }

    // ✅ Caso exitoso de creación de usuario
    @Test
    void testCreateUser_Success() throws Exception {
        when(userRepository.findByEmailOrDocumentNumber(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(userMapper.toEntity(any(RequestUserDTO.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(new ResponseUserDTO());

        ResponseEntity<ResponseDTO> response = userService.createUser(requestUserDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(201, response.getBody().getCodigo());
        verify(emailService, times(1)).SendMailHome(eq(user.getEmail()), anyString());
    }

    // ❌ Usuario duplicado
    @Test
    void testCreateUser_Duplicate() throws Exception {
        when(userRepository.findByEmailOrDocumentNumber(anyString(), anyString()))
                .thenReturn(Optional.of(user));

        ResponseEntity<ResponseDTO> response = userService.createUser(requestUserDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getCodigo());
        verify(userRepository, never()).save(any());
    }

    // ❌ Dominio inválido
    @Test
    void testCreateUser_InvalidDomain() throws Exception {
        requestUserDTO.setEmail("juan@gmail.com"); // dominio inválido
        when(userRepository.findByEmailOrDocumentNumber(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(userMapper.toEntity(any(RequestUserDTO.class))).thenReturn(user);

        ResponseEntity<ResponseDTO> response = userService.createUser(requestUserDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getCodigo());
    }

    // ✅ Enviar código de confirmación
    @Test
    void testSendCodeConfirmation() throws Exception {
        ResponseEntity<ResponseDTO> response = userService.sendCodeConfirmation("test@uniquindio.edu.co");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCodigo());
        assertTrue(response.getBody().getMensaje().contains("Código enviado"));
    }

    // ✅ Verificar cuenta por código
    @Test
    void testVerifyAccountEmailCode() throws Exception {
        ResponseEntity<ResponseDTO> response = userService.verifyAccountEmailCode("test@uniquindio.edu.co", "1234");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCodigo());
        assertEquals("Cuenta verificada correctamente", response.getBody().getMensaje());
    }

    // ✅ Verificar usuario por token válido
    @Test
    void testVerifyUserByToken_Success() {
        when(userRepository.findByVerificationToken(anyString())).thenReturn(Optional.of(user));

        ResponseEntity<ResponseDTO> response = userService.verifyUserByToken("validToken");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCodigo());
        assertTrue(user.isVerification());
        verify(userRepository, times(1)).save(user);
    }

    // ❌ Verificar usuario por token inválido
    @Test
    void testVerifyUserByToken_Invalid() {
        when(userRepository.findByVerificationToken(anyString())).thenReturn(Optional.empty());

        ResponseEntity<ResponseDTO> response = userService.verifyUserByToken("invalidToken");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getCodigo());
    }

    // ✅ Verificar usuario ya verificado
    @Test
    void testVerifyUserByToken_AlreadyVerified() {
        user.setVerification(true);
        when(userRepository.findByVerificationToken(anyString())).thenReturn(Optional.of(user));

        ResponseEntity<ResponseDTO> response = userService.verifyUserByToken("alreadyVerified");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCodigo());
        assertEquals("La cuenta ya había sido verificada anteriormente", response.getBody().getMensaje());
    }
}
