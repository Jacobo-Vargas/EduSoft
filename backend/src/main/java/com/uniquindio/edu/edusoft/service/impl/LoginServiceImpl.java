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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.uniquindio.edu.edusoft.model.entities.User;

import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final LoginMapper loginMapper;
    private final JwtService jwtService;
    private final TokenStoreService tokenStoreService;

    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginRequest) throws Exception {
        User user;
        if(!loginRequest.getUsername().isEmpty()){
            if(validateEmailDomain(loginRequest.getUsername())){
                user = loginRepository.findByEmail(loginRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Usuario no encontrado: " + loginRequest.getUsername()));
                // 1. Genear token
                String accessToken = jwtService.generateToken(user.getEmail());
                // 1.1 extraer jti
                String jti = jwtService.extractJti(accessToken);
                // 2. Guardar token en Redis con TTL
                tokenStoreService.storeToken(jti, user.getEmail());
                return ResponseEntity.ok(new AuthResponseDTO(accessToken));
            } else if (validateCellPhoneNumber(loginRequest.getUsername())) {
                user = loginRepository.findByPhone(loginRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Usuario no encontrado: " + loginRequest.getUsername()));
                // 1. Genear token
                String accessToken = jwtService.generateToken(user.getPhone());
                // 1.1 extraer jti
                String jti = jwtService.extractJti(accessToken);
                // 2. Guardar token en Redis con TTL
                tokenStoreService.storeToken(jti, user.getPhone());
                return ResponseEntity.ok(new AuthResponseDTO(accessToken));
            }
        }else{
            throw new IllegalArgumentException("Username debe ser correo o teléfono válido");
        }
        return null;
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