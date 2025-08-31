package com.uniquindio.edu.edusoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.edu.edusoft.controller.UserController;
import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.uniquindio.edu.edusoft.config.security.JwtAuthFilter.class)
        })
@AutoConfigureMockMvc(addFilters = false) // 🚫 sin filtros de seguridad
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        RequestUserDTO dto = new RequestUserDTO();
        dto.setDocumentNumber("123");
        dto.setName("Juan");
        dto.setPhone("3101234567");
        dto.setAddress("Calle 123");
        dto.setEmail("juan@uqvirtual.edu.co");
        dto.setPassword("password123");

        when(userService.createUser(any(RequestUserDTO.class)))
                .thenReturn(ResponseEntity.status(201)
                        .body(new ResponseDTO(201, "Usuario creado exitosamente", null)));

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value(201))
                .andExpect(jsonPath("$.mensaje").value("Usuario creado exitosamente"));
    }



    @Test
    void testSendCodeConfirmation() throws Exception {
        String email = "test@uniquindio.edu.co";

        when(userService.sendCodeConfirmation(email))
                .thenReturn(ResponseEntity.ok(new ResponseDTO(200, "Código enviado al correo " + email, null)));

        mockMvc.perform(post("/users/sendCodeConfirmation/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(200))
                .andExpect(jsonPath("$.mensaje").value("Código enviado al correo " + email));
    }

    @Test
    void testVerifyAccountEmailCode() throws Exception {
        String email = "test@uniquindio.edu.co";
        String code = "123456";

        when(userService.verifyAccountEmailCode(email, code))
                .thenReturn(ResponseEntity.ok(new ResponseDTO(200, "Cuenta verificada correctamente", null)));

        mockMvc.perform(put("/users/verifyAccountEmailCode/{email}/{code}", email, code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(200))
                .andExpect(jsonPath("$.mensaje").value("Cuenta verificada correctamente"));
    }

    @Test
    void testVerifyUser_Success() throws Exception {
        String token = "validToken";

        // El servicio no devuelve nada en este método, pero lo mockeamos igual
        when(userService.verifyUserByToken(token))
                .thenReturn(ResponseEntity.ok(new ResponseDTO(200, "¡Cuenta verificada exitosamente!", null)));

        mockMvc.perform(get("/users/verify")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/html; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("html"))); // verifica que es HTML
    }

    @Test
    void testVerifyUser_Failure() throws Exception {
        String token = "invalidToken";

        // simulamos excepción
        when(userService.verifyUserByToken(token))
                .thenThrow(new RuntimeException("Token inválido"));

        mockMvc.perform(get("/users/verify")
                        .param("token", token))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-Type", "text/html; charset=UTF-8"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("html")));
    }

}
