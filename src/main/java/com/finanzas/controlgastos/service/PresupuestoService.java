package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.PresupuestoRequest;
import com.finanzas.controlgastos.dto.response.PresupuestoResponse;
import com.finanzas.controlgastos.exception.RecursoNoEncontradoException;
import com.finanzas.controlgastos.model.Presupuesto;
import com.finanzas.controlgastos.model.Usuario;
import com.finanzas.controlgastos.repository.PresupuestoRepository;
import com.finanzas.controlgastos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresupuestoService implements IPresupuestoService {

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<PresupuestoResponse> obtenerTodos() {
        return presupuestoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PresupuestoResponse> obtenerPorId(Integer id) {
        return presupuestoRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public List<PresupuestoResponse> obtenerPorUsuario(Integer usuarioId) {
        return presupuestoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PresupuestoResponse guardar(PresupuestoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + request.getUsuarioId()));
        Presupuesto presupuesto = new Presupuesto(
                request.getDescripcion(),
                request.getSaldoDisponible(),
                request.getFechaInicio(),
                request.getFechaFin(),
                usuario
        );
        return convertirAResponse(presupuestoRepository.save(presupuesto));
    }

    @Override
    public PresupuestoResponse actualizar(Integer id, PresupuestoRequest request) {
        Presupuesto presupuesto = presupuestoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Presupuesto no encontrado con id: " + id));
        presupuesto.setDescripcion(request.getDescripcion());
        presupuesto.setSaldoDisponible(request.getSaldoDisponible());
        presupuesto.setFechaInicio(request.getFechaInicio());
        presupuesto.setFechaFin(request.getFechaFin());
        return convertirAResponse(presupuestoRepository.save(presupuesto));
    }

    @Override
    public void eliminar(Integer id) {
        if (!presupuestoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Presupuesto no encontrado con id: " + id);
        }
        presupuestoRepository.deleteById(id);
    }

    private PresupuestoResponse convertirAResponse(Presupuesto presupuesto) {
        PresupuestoResponse response = new PresupuestoResponse();
        response.setId(presupuesto.getId());
        response.setDescripcion(presupuesto.getDescripcion());
        response.setSaldoDisponible(presupuesto.getSaldoDisponible());
        response.setFechaInicio(presupuesto.getFechaInicio());
        response.setFechaFin(presupuesto.getFechaFin());
        response.setUsuarioId(presupuesto.getUsuario() != null ? presupuesto.getUsuario().getId() : null);
        return response;
    }
}
