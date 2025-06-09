package com.servAuth.config;

import com.servAuth.model.Rol;
import com.servAuth.model.Usuario;
import com.servAuth.repository.RolRepository;
import com.servAuth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;
@Component
public class DatabaseInitializer implements CommandLineRunner{
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        inicializarRoles();
        crearAdminPorDefecto();
    }


    private void inicializarRoles() {
        if (rolRepository.count() == 0) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ROL_ADMIN");
            rolRepository.save(rolAdmin);

            Rol rolCliente = new Rol();
            rolCliente.setNombre("ROL_CLIENTE");
            rolRepository.save(rolCliente);

            System.out.println("Roles inicializados en la base de datos");
        }
    }


    private void crearAdminPorDefecto() {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gmail.com");
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setActivo(true);

            Set<Rol> roles = new HashSet<>();
            Rol rolAdmin = rolRepository.findByNombre("ROL_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(rolAdmin);
            admin.setRoles(roles);

            usuarioRepository.save(admin);

            System.out.println("Usuario administrador creado: admin / admin123");
        }
    }
}
