package com.servAuth.service;

import com.servAuth.dto.UsuarioDTO;
import com.servAuth.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<UsuarioDTO> obtenerTodos();
    Optional<UsuarioDTO> obtenerPorId(Long id);
    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);
    void eliminarUsuario(Long id);
    void desactivarUsuario(Long id);
}

