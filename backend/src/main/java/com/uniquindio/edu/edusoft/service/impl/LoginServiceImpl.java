package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.config.security.TokenStoreService;
import com.uniquindio.edu.edusoft.model.entities.CodeChangePassword;
import com.uniquindio.edu.edusoft.model.mapper.LoginMapper;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.repository.CodeChangePasswordRepository;
import com.uniquindio.edu.edusoft.repository.LoginRepository;
import com.uniquindio.edu.edusoft.service.LoginService;
import com.uniquindio.edu.edusoft.utils.BaseResponse;
import com.uniquindio.edu.edusoft.utils.CodeGenerator;
import com.uniquindio.edu.edusoft.utils.ResponseData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.uniquindio.edu.edusoft.model.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final LoginMapper loginMapper;
    private final EmailServiceImpl emailService;
    private final JwtService jwtService;
    private final TokenStoreService tokenStoreService;
    private final PasswordEncoder passwordEncoder;
    private final CodeChangePasswordRepository codeChangePasswordRepository;

    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginRequest, HttpServletResponse response) {
        String userInput = loginRequest.getUsername() == null ? "" : loginRequest.getUsername().trim();
        String rawPassword = loginRequest.getPassword();

        if (userInput.isEmpty() || rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Username y password son obligatorios");
        }

        // 1) Buscar por email o por teléfono
        User user;
        if (validateEmailDomain(userInput)) {
            user = loginRepository.findByEmail(userInput.toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        } else if (validateCellPhoneNumber(userInput)) {
            user = loginRepository.findByPhone(userInput)
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        } else {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        if (!user.isVerification()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Cuenta no verificada"));
        }

        // Comparar password
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String subject = user.getEmail();
        String accessToken = jwtService.generateToken(subject, user.getUserType().toString(), user.getId());
        String jti = jwtService.extractJti(accessToken);

        tokenStoreService.storeToken(jti, subject);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60); // 15 minutos
        response.addCookie(accessCookie);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("userType", user.getUserType().toString());
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());

        ResponseData<Map<String, Object>> response1 = new ResponseData<>(
                userData, // Los datos del usuario van aquí
                "Inicio de sesión correcto",
                "success"
        );

        return ResponseEntity.ok(response1);
    }

    @Override
    public ResponseEntity<?> sendCodeEmail(LoginRequestDTO loginRequest, HttpServletResponse response) throws Exception {
        // Validar que el username no sea nulo ni vacío
        String userInput = loginRequest.getUsername() == null ? "" : loginRequest.getUsername().trim();
        if (userInput.isEmpty()) {
            throw new IllegalArgumentException("El campo 'Username' es obligatorio");
        }
        // Validar si el username es un correo electrónico
        User user = null;
        if (validateEmailDomain(userInput)) {
            // Buscar el usuario por su correo electrónico en la base de datos
            user = loginRepository.findByEmail(userInput.toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
            // Si el usuario es encontrado, podemos proceder a enviar el código de verificación
            String verificationCode = CodeGenerator.generateCode(); // Generamos el código de verificación
            emailService.sendCodeVerifactionPassword(user.getEmail(), verificationCode); // Enviamos el correo
            // Buscar si el usuario ya tiene un código de cambio de contraseña
            Optional<CodeChangePassword> existingCode = codeChangePasswordRepository.findByUser(user);
            // Si ya existe un código, lo actualizamos
            if (existingCode.isPresent()) {
                CodeChangePassword codeChangePassword = existingCode.get();
                codeChangePassword.setCode(verificationCode);
                codeChangePassword.setExpirationTime(System.currentTimeMillis() + 10 * 60 * 100); // Expira en 10 minutos
                codeChangePasswordRepository.save(codeChangePassword);
            } else {
                // Si no existe, creamos un nuevo código de cambio de contraseña
                CodeChangePassword codeChangePassword = new CodeChangePassword();
                codeChangePassword.setCode(verificationCode);
                codeChangePassword.setUser(user);
                codeChangePassword.setExpirationTime(System.currentTimeMillis() + 10 * 60 * 100); // Expira en 10 minutos
                codeChangePasswordRepository.save(codeChangePassword);
            }
            return ResponseEntity.ok(verificationCode);
        }
        else if (validateCellPhoneNumber(userInput)) {
                // Buscar el usuario por su correo electrónico en la base de datos
                user = loginRepository.findByPhone(userInput.toLowerCase())
                        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
                // Si el usuario es encontrado, podemos proceder a enviar el código de verificación
                String verificationCode = CodeGenerator.generateCode(); // Generamos el código de verificación
                emailService.sendCodeVerifactionPassword(user.getEmail(), verificationCode);
            // Buscar si el usuario ya tiene un código de cambio de contraseña
            Optional<CodeChangePassword> existingCode = codeChangePasswordRepository.findByUser(user);// Enviamos el correo
            if (existingCode.isPresent()) {
                CodeChangePassword codeChangePassword = existingCode.get();
                codeChangePassword.setCode(verificationCode);
                codeChangePassword.setExpirationTime(System.currentTimeMillis() + 10 * 60 * 100); // Expira en 10 minutos
                codeChangePasswordRepository.save(codeChangePassword);
            } else {
                // Si no existe, creamos un nuevo código de cambio de contraseña
                CodeChangePassword codeChangePassword = new CodeChangePassword();
                codeChangePassword.setCode(verificationCode);
                codeChangePassword.setUser(user);
                codeChangePassword.setExpirationTime(System.currentTimeMillis() + 10 * 60 * 100); // Expira en 10 minutos
                codeChangePasswordRepository.save(codeChangePassword);
            }
                return ResponseEntity.ok(verificationCode);
        } else {
            // Si el dominio del email no es válido, lanzar una excepción o retornar un mensaje adecuado
            throw new IllegalArgumentException("Dominio de correo no válido");
        }
    }




    @Override
    public ResponseEntity<?> logout(String token, HttpServletResponse response) throws Exception {
        if (token == null) {
            return BaseResponse.response(null, "No hay sesión activa", "error", HttpStatus.UNAUTHORIZED);
        }

        Claims claims = jwtService.extractAllClaims(token);
        String jti = jwtService.extractJti(token);
        tokenStoreService.removeToken(jti);

        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return BaseResponse.response("Se cerró la sesión con éxito", "success");
    }

    @Override
    public ResponseEntity<?> updatePassword(LoginRequestDTO loginRequest) throws Exception {
        // Validar que el username no sea nulo ni vacío
        String userInput = loginRequest.getUsername() == null ? "" : loginRequest.getUsername().trim();
        if (userInput.isEmpty()) {
            throw new IllegalArgumentException("El campo 'Username' es obligatorio");
        }
        // Validar si el username es un correo electrónico
        User user = null;
        if (validateEmailDomain(userInput)) {
            // Buscar el usuario por su correo electrónico en la base de datos
            user = loginRepository.findByEmail(userInput.toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
            loginRepository.save(user);
            ResponseData<String> response = new ResponseData<>(null, "Se ha cambiado correctamente la contraseña", "success");
            return ResponseEntity.ok(response);
        }
        else if (validateCellPhoneNumber(userInput)) {
            // Buscar el usuario por su correo electrónico en la base de datos
            user = loginRepository.findByPhone(userInput.toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
            loginRepository.save(user);
            ResponseData<String> response = new ResponseData<>(null, "Se ha cambiado correctamente la contraseña", "success");
            return ResponseEntity.ok(response);
        } else {
            // Si el dominio del email no es válido, lanzar una excepción o retornar un mensaje adecuado
            throw new IllegalArgumentException("Dominio de correo o formato de numero celular no válido");
        }
    }

    @Override
    public ResponseEntity<?> verifyCode(String code, LoginRequestDTO loginRequest) throws Exception {
        // Validar que el username no sea nulo ni vacío
        String userInput = loginRequest.getUsername() == null ? "" : loginRequest.getUsername().trim();
        if (userInput.isEmpty()) {
            throw new IllegalArgumentException("El campo 'Username' es obligatorio");
        }
        // Buscar el usuario por su correo electrónico o teléfono
        User user = null;
        if (validateEmailDomain(userInput)) {
            // Buscar el usuario por correo electrónico
            user = loginRepository.findByEmail(userInput.toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        } else if (validateCellPhoneNumber(userInput)) {
            // Buscar el usuario por teléfono
            user = loginRepository.findByPhone(userInput.toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        } else {
            throw new IllegalArgumentException("Dominio de correo no válido");
        }
        // Buscar el código de cambio de contraseña asociado al usuario
        Optional<CodeChangePassword> existingCodeOpt = codeChangePasswordRepository.findByUser(user);
        // Verificar si el usuario tiene un código de cambio de contraseña
        if (existingCodeOpt.isPresent()) {
            CodeChangePassword existingCode = existingCodeOpt.get();
            // Verificar si el código enviado desde el frontend coincide con el guardado
            if (!existingCode.getCode().equals(code)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código no es válido.");
            }
            ResponseData<String> response = new ResponseData<>(null, "Código de validación correcto.", "success");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontró un código de validación para este usuario.");
        }
    }


    public Boolean validateEmailDomain(String email){
        Predicate<String> domainPredicate = e -> e != null && (e.endsWith("@uqvirtual.edu.co") || e.endsWith("@uniquindio.edu.co") || e.endsWith("@gmail.com"));
        return domainPredicate.test(email);
    }

    public Boolean validateCellPhoneNumber(String number){
        Predicate<String> numberFormatPredicate = e -> e != null && e.length() == 10 && e.startsWith("3");
        return numberFormatPredicate.test(number);
    }
}