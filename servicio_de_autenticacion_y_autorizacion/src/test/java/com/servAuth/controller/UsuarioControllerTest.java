package com.servAuth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servAuth.dto.UsuarioDTO;
import com.servAuth.service.IUsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false) // Evita errores de seguridad con JWT
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private IUsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearUsuario_retorna201() throws Exception {
        UsuarioDTO dto = UsuarioDTO.builder()
                .username("maria123")
                .email("maria@example.com")
                .nombre("Maria")
                .apellido("Lopez")
                .activo(true)
                .build();

        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
