package com.uniquindio.edu.edusoft.Auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.edusoft.security.JwtService;
import com.uniquindio.edu.edusoft.security.TokenStoreService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenStoreService tokenStoreService;

    @Autowired
    private JwtService jwtService;

    @Test
    @SuppressWarnings("unchecked")
    void testLoginAndLogoutFlow() throws Exception {

        // 1. Se simula parámetros enviados de login
        // FIXME -> se deben de recibir encriptados para cumplir con atributos de seguridad e integridad de datos
        String loginJson = """
            {
              "username": "admin",
              "password": "1234"
            }
        """;

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isNotBlank();
        log.info("Response: login satisfactorio");


        Map<String, Object> tokenMap = new ObjectMapper().readValue(response, Map.class);
        String token = tokenMap.get("accessToken").toString();
        log.info("Token extraido: {}", token);


        // 2. Se verifica que el token quedó guardado en Redis
        String jti = jwtService.extractJti(token);
        assertThat(tokenStoreService.isTokenValid(jti)).isTrue();
        log.info("Verificación de token en redis con jti finalizada");

        // 3. Logout se elimina el token, internamente con jti
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        log.info("Simular cierre de sesión eliminando jti de redis exitoso");

        // 4. Token debe estar eliminado
        assertThat(tokenStoreService.isTokenValid(jti)).isFalse();
        log.info("Validación de cierre de sesión eliminando de redis exitoso");

        // 5. Intentar acceder con el token eliminado → debería rechazar
        mockMvc.perform(post("/api/prueba")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        log.info("Simular nuevo inicio de sesión con token vencido exitoso");
    }
}
