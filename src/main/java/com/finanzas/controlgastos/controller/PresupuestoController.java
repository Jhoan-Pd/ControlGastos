package com.finanzas.controlgastos.controller;

import com.finanzas.controlgastos.dto.request.PresupuestoRequest;
import com.finanzas.controlgastos.dto.response.PresupuestoResponse;
import com.finanzas.controlgastos.service.IPresupuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presupuestos")
public class PresupuestoController {

    @Autowired
    private IPresupuestoService presupuestoService;

    @GetMapping
    public ResponseEntity<List<PresupuestoResponse>> obtenerTodos() {
        return ResponseEntity.ok(presupuestoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> obtenerPorId(@PathVariable Integer id) {
        return presupuestoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PresupuestoResponse>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(presupuestoService.obtenerPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<PresupuestoResponse> crear(@RequestBody PresupuestoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(presupuestoService.guardar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresupuestoResponse> actualizar(@PathVariable Integer id,
                                                          @RequestBody PresupuestoRequest request) {
        return ResponseEntity.ok(presupuestoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        presupuestoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
