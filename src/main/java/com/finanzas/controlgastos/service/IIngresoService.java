package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.IngresoRequest;
import com.finanzas.controlgastos.dto.response.IngresoResponse;

import java.util.List;
import java.util.Optional;

// Contrato de operaciones para la gestión de ingresos
public interface IIngresoService {

    List<IngresoResponse> obtenerTodos();

    Optional<IngresoResponse> obtenerPorId(Integer id);

    List<IngresoResponse> obtenerPorUsuario(Integer usuarioId);

    IngresoResponse guardar(IngresoRequest request);

    IngresoResponse actualizar(Integer id, IngresoRequest request);

    void eliminar(Integer id);
}
