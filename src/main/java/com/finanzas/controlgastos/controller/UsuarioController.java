package com.finanzas.controlgastos.controller;

import com.finanzas.controlgastos.dto.request.LoginRequest;
import com.finanzas.controlgastos.dto.request.UsuarioRequest;
import com.finanzas.controlgastos.dto.response.UsuarioResponse;
import com.finanzas.controlgastos.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Autentica un usuario por email y contraseña
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest request) {
        return usuarioService.login(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Integer id,
                                                      @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
