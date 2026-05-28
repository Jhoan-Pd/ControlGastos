package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.LoginRequest;
import com.finanzas.controlgastos.dto.request.UsuarioRequest;
import com.finanzas.controlgastos.dto.response.UsuarioResponse;

import java.util.List;
import java.util.Optional;

// Contrato de operaciones para la gestión de usuarios
public interface IUsuarioService {

    List<UsuarioResponse> obtenerTodos();

    Optional<UsuarioResponse> obtenerPorId(Integer id);

    Optional<UsuarioResponse> obtenerPorEmail(String email);

    // Valida credenciales y retorna el usuario si son correctas
    Optional<UsuarioResponse> login(LoginRequest request);

    UsuarioResponse guardar(UsuarioRequest request);

    UsuarioResponse actualizar(Integer id, UsuarioRequest request);

    void eliminar(Integer id);
}
