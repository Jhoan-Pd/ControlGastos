package com.finanzas.controlgastos.controller;

import com.finanzas.controlgastos.dto.request.IngresoRequest;
import com.finanzas.controlgastos.dto.response.IngresoResponse;
import com.finanzas.controlgastos.service.IIngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingresos")
public class IngresoController {

    @Autowired
    private IIngresoService ingresoService;

    @GetMapping
    public ResponseEntity<List<IngresoResponse>> obtenerTodos() {
        return ResponseEntity.ok(ingresoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoResponse> obtenerPorId(@PathVariable Integer id) {
        return ingresoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<IngresoResponse>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(ingresoService.obtenerPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<IngresoResponse> crear(@RequestBody IngresoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingresoService.guardar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngresoResponse> actualizar(@PathVariable Integer id,
                                                      @RequestBody IngresoRequest request) {
        return ResponseEntity.ok(ingresoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ingresoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
