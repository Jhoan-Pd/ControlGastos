package com.finanzas.controlgastos.controller;

import com.finanzas.controlgastos.dto.request.CategoriaRequest;
import com.finanzas.controlgastos.dto.response.CategoriaResponse;
import com.finanzas.controlgastos.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    // Retorna las categorías raíz con sus subcategorías anidadas
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodasRaiz());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Integer id) {
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.guardar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Integer id,
                                                        @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
