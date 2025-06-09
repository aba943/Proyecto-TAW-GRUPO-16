package com.servAuth.service;

import com.servAuth.dto.UsuarioDTO;
import com.servAuth.model.Usuario;
import com.servAuth.repository.UsuarioRepository;
import com.servAuth.service.Impl.UsuarioServiceImpl;

import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Test
    void obtenerUsuarioPorId_devuelveDTO() {
        // Mockear dependencias
        UsuarioRepository repo = mock(UsuarioRepository.class);
        UsuarioServiceImpl service = new UsuarioServiceImpl(repo); // Asegúrate que uses este constructor

        // Datos simulados
        Usuario usuario = Usuario.builder()
                .id(1L)
                .username("juan123")
                .email("juan@example.com")
                .nombre("Juan")
                .apellido("Pérez")
                .activo(true)
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(usuario));

        // Ejecutar método
        Optional<UsuarioDTO> resultado = service.obtenerPorId(1L);

        // Verificar resultado
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getUsername()).isEqualTo("juan123");
    }
}
