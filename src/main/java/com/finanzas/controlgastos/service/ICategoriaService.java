package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.CategoriaRequest;
import com.finanzas.controlgastos.dto.response.CategoriaResponse;

import java.util.List;
import java.util.Optional;

// Contrato de operaciones para la gestión de categorías
public interface ICategoriaService {

    // Retorna solo las categorías raíz con sus subcategorías anidadas
    List<CategoriaResponse> obtenerTodasRaiz();

    Optional<CategoriaResponse> obtenerPorId(Integer id);

    CategoriaResponse guardar(CategoriaRequest request);

    CategoriaResponse actualizar(Integer id, CategoriaRequest request);

    void eliminar(Integer id);
}
