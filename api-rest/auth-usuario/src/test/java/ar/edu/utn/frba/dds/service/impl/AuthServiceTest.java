package ar.edu.utn.frba.dds.service.impl;

import ar.edu.utn.frba.dds.dtos.LoginRequestDTO;
import ar.edu.utn.frba.dds.dtos.SignUpRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DataSource dataSource;

  private SignUpRequestDTO signUpRequest;
  private LoginRequestDTO loginRequest;

  @BeforeEach
  void setUp() throws Exception {
    // Log para confirmar conexión a H2
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("🔍 Conectado a base de datos: " + connection.getMetaData().getURL());
    }

    signUpRequest = new SignUpRequestDTO("usuarioTest", "pass123", "usuario@gmail.com", "usuarioNombre");
    loginRequest = new LoginRequestDTO("usuarioTest", "pass123");
  }

  @Test
  void testSignUpYLoginExitoso() throws Exception {
    // --- SIGNUP ---
    mockMvc.perform(post("/api/auth/register") // ✅ Asegurate que coincida con tu endpoint real
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isSuccess").value(true));

    // --- LOGIN ---
    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists());
  }

  @Test
  void testSignUp_FallaCuandoUsuarioYaExiste() throws Exception {
    // Crear usuario
    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signUpRequest)));

    // Intentar crearlo de nuevo
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)))
        .andExpect(status().isBadRequest()) // Asegurate de tener un @ExceptionHandler para IllegalArgumentException
        .andExpect(content().string(containsString("ya está en uso")));
  }
}
