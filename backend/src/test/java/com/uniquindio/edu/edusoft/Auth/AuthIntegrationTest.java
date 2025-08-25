package com.uniquindio.edu.edusoft.Auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void testLoginAndLogoutFlow() throws Exception {
        String email = "usuario@correo.com";
        String password = "123456";

        // 1. Login → debería crear cookie HttpOnly y guardar token en Redis
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String setCookieHeader = loginResult.getResponse().getHeader("Set-Cookie");
        assertNotNull(setCookieHeader, "El login debe devolver una cookie");
        assertTrue(setCookieHeader.contains("HttpOnly"), "La cookie debe ser HttpOnly");

        // Extraer token desde Redis usando el email como key
        String tokenInRedis = redisTemplate.opsForValue().get("session:" + email);
        assertNotNull(tokenInRedis, "El token debe estar guardado en Redis");

        // 2. Usar la cookie para acceder a un endpoint protegido
        mockMvc.perform(get("/api/protegido")
                        .header("Cookie", setCookieHeader))
                .andExpect(status().isOk());

        // 3. Logout → debería borrar cookie y eliminar token de Redis
        mockMvc.perform(post("/api/auth/logout")
                        .header("Cookie", setCookieHeader))
                .andExpect(status().isOk());

        String tokenAfterLogout = redisTemplate.opsForValue().get("session:" + email);
        assertNull(tokenAfterLogout, "El token debe eliminarse de Redis tras logout");

        // 4. Intentar acceder de nuevo con la misma cookie → debe fallar
        mockMvc.perform(get("/api/protegido")
                        .header("Cookie", setCookieHeader))
                .andExpect(status().isUnauthorized());
    }
}
