package com.servAuth.controller;

import com.servAuth.dto.AuthDTO.JwtResponse;
import com.servAuth.dto.AuthDTO.LoginRequest;
import com.servAuth.dto.AuthDTO.MessageResponse;
import com.servAuth.dto.AuthDTO.SignupRequest;
import com.servAuth.model.Rol;
import com.servAuth.model.Usuario;
import com.servAuth.repository.RolRepository;
import com.servAuth.repository.UsuarioRepository;
import com.servAuth.security.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        return ResponseEntity.ok(new JwtResponse(jwt,
                usuario.getId(),
                userDetails.getUsername(),
                usuario.getEmail(),
                new HashSet<>(roles)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario ya está en uso."));
        }

        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El email ya está en uso."));
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(signUpRequest.getUsername());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setPassword(encoder.encode(signUpRequest.getPassword()));
        usuario.setNombre(signUpRequest.getNombre());
        usuario.setApellido(signUpRequest.getApellido());


        Set<String> strRoles = signUpRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null) {
            Rol clienteRol = rolRepository.findByNombre("ROL_CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(clienteRol);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Rol adminRol = rolRepository.findByNombre("ROL_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRol);
                        break;
                    case " ":

                        break;
                    default:
                        Rol clienteRol = rolRepository.findByNombre("ROL_CLIENTE")
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(clienteRol);
                }
            });
        }

        usuario.setRoles(roles);
        usuarioRepository.save(usuario);


        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada exitosamente!"));
    }

    //Validacion de token JWT
    @GetMapping("/validate")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Falta o formato incorrecto del token");
        }

        String token = authHeader.substring(7);

        if (!jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Opcional: obtener más datos del usuario autenticado desde el contexto
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok("Token válido para el usuario: " + username);
    }




}
