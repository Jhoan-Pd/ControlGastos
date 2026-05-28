package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.PresupuestoRequest;
import com.finanzas.controlgastos.dto.response.PresupuestoResponse;

import java.util.List;
import java.util.Optional;

// Contrato de operaciones para la gestión de presupuestos
public interface IPresupuestoService {

    List<PresupuestoResponse> obtenerTodos();

    Optional<PresupuestoResponse> obtenerPorId(Integer id);

    List<PresupuestoResponse> obtenerPorUsuario(Integer usuarioId);

    PresupuestoResponse guardar(PresupuestoRequest request);

    PresupuestoResponse actualizar(Integer id, PresupuestoRequest request);

    void eliminar(Integer id);
}
