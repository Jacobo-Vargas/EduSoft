package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.config.security.TokenStoreService;
import com.uniquindio.edu.edusoft.model.mapper.LoginMapper;
import com.uniquindio.edu.edusoft.model.dto.respose.AuthResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.repository.LoginRepository;
import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.config.security.TokenStoreService;
import com.uniquindio.edu.edusoft.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.uniquindio.edu.edusoft.model.entities.User;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final LoginMapper loginMapper;
    private final JwtService jwtService;
    private final TokenStoreService tokenStoreService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginRequest) {
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
        if (Boolean.FALSE.equals(user.isVerification())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Cuenta no verificada"));
        }
        // Comparar password
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        // Generar token (sug.: usa ID como 'sub' para unificar email/phone)
        String subject = String.valueOf(user.getEmail()); // o user.getEmail()
        String accessToken = jwtService.generateToken(subject);
        String jti = jwtService.extractJti(accessToken);
        // Guardar jti en Redis con TTL (maneja Redis caído con tu @ControllerAdvice)
        tokenStoreService.storeToken(jti, subject);

        return ResponseEntity.ok(new AuthResponseDTO(accessToken));
    }
    public Boolean validateEmailDomain(String email){
        Predicate<String> domainPredicate = e -> e != null && (e.endsWith("@uqvirtual.edu.co") || e.endsWith("@uniquindio.edu.co"));
        return domainPredicate.test(email);
    }
    public Boolean validateCellPhoneNumber(String number){
        Predicate<String> numberFormatPredicate = e -> e != null && e.length() == 10 && e.startsWith("3");
        return numberFormatPredicate.test(number);
    }
}