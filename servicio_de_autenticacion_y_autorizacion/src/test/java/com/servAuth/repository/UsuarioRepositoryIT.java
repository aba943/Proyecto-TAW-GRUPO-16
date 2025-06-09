package com.servAuth.repository;

import com.servAuth.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void guardarYBuscarUsuarioPorUsername() {
        Usuario usuario = Usuario.builder()
                .username("admin123")
                .password("secreto123")
                .email("admin@example.com")
                .nombre("Administrador")
                .apellido("Principal")
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        Optional<Usuario> encontrado = usuarioRepository.findByUsername("admin123");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("admin@example.com");
    }

    @Test
    void existeUsernameYEmail() {
        Usuario usuario = Usuario.builder()
                .username("usuario1")
                .password("clave123")
                .email("usuario1@example.com")
                .nombre("Usuario")
                .apellido("Uno")
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        assertThat(usuarioRepository.existsByUsername("usuario1")).isTrue();
        assertThat(usuarioRepository.existsByEmail("usuario1@example.com")).isTrue();
    }
}
