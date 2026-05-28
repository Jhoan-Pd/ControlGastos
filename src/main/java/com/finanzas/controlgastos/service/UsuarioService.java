package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.LoginRequest;
import com.finanzas.controlgastos.dto.request.UsuarioRequest;
import com.finanzas.controlgastos.dto.response.UsuarioResponse;
import com.finanzas.controlgastos.exception.RecursoNoEncontradoException;
import com.finanzas.controlgastos.model.Usuario;
import com.finanzas.controlgastos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioResponse> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public Optional<UsuarioResponse> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::convertirAResponse);
    }

    @Override
    public Optional<UsuarioResponse> login(LoginRequest request) {
        return usuarioRepository.findByEmail(request.getEmail())
                .filter(u -> u.getContrasena().equals(request.getContrasena()))
                .map(this::convertirAResponse);
    }

    @Override
    public UsuarioResponse guardar(UsuarioRequest request) {
        Usuario usuario = new Usuario(request.getNombre(), request.getEmail(), request.getContrasena());
        return convertirAResponse(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse actualizar(Integer id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(request.getContrasena());
        }
        return convertirAResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setEmail(usuario.getEmail());
        return response;
    }
}
