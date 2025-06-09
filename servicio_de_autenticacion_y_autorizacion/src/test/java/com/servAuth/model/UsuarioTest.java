package com.servAuth.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void usuarioConEmailInvalidoNoEsValido() {
        Usuario usuario = Usuario.builder()
                .username("usuario1")
                .password("clave123")
                .email("correo-invalido")  // <== aquí está el error
                .nombre("Ana")
                .apellido("Gómez")
                .activo(true)
                .build();

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("email"));
    }

    @Test
    void usuarioSinNombreDeberiaSerInvalido() {
        Usuario usuario = Usuario.builder()
                .username("usuario2")
                .password("clave123")
                .email("usuario2@example.com")
                .nombre("")  // <== vacío
                .apellido("Gómez")
                .activo(true)
                .build();

        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);

        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("nombre"));
    }
}
