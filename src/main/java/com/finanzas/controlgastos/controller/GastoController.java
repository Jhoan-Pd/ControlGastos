package com.finanzas.controlgastos.controller;

import com.finanzas.controlgastos.dto.request.GastoRequest;
import com.finanzas.controlgastos.dto.response.GastoResponse;
import com.finanzas.controlgastos.service.IGastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    @Autowired
    private IGastoService gastoService;

    @GetMapping
    public ResponseEntity<List<GastoResponse>> obtenerTodos() {
        return ResponseEntity.ok(gastoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponse> obtenerPorId(@PathVariable Integer id) {
        return gastoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<GastoResponse>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(gastoService.obtenerPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<GastoResponse> crear(@RequestBody GastoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gastoService.guardar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GastoResponse> actualizar(@PathVariable Integer id,
                                                    @RequestBody GastoRequest request) {
        return ResponseEntity.ok(gastoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        gastoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
