package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.GastoRequest;
import com.finanzas.controlgastos.dto.response.GastoResponse;

import java.util.List;
import java.util.Optional;

// Contrato de operaciones para la gestión de gastos
public interface IGastoService {

    List<GastoResponse> obtenerTodos();

    Optional<GastoResponse> obtenerPorId(Integer id);

    List<GastoResponse> obtenerPorUsuario(Integer usuarioId);

    GastoResponse guardar(GastoRequest request);

    GastoResponse actualizar(Integer id, GastoRequest request);

    void eliminar(Integer id);
}
