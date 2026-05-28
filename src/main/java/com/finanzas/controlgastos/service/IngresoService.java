package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.IngresoRequest;
import com.finanzas.controlgastos.dto.response.IngresoResponse;
import com.finanzas.controlgastos.exception.RecursoNoEncontradoException;
import com.finanzas.controlgastos.model.Categoria;
import com.finanzas.controlgastos.model.Ingreso;
import com.finanzas.controlgastos.model.Presupuesto;
import com.finanzas.controlgastos.model.Usuario;
import com.finanzas.controlgastos.repository.CategoriaRepository;
import com.finanzas.controlgastos.repository.IngresoRepository;
import com.finanzas.controlgastos.repository.PresupuestoRepository;
import com.finanzas.controlgastos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngresoService implements IIngresoService {

    @Autowired
    private IngresoRepository ingresoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Override
    public List<IngresoResponse> obtenerTodos() {
        return ingresoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IngresoResponse> obtenerPorId(Integer id) {
        return ingresoRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public List<IngresoResponse> obtenerPorUsuario(Integer usuarioId) {
        return ingresoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IngresoResponse guardar(IngresoRequest request) {
        Ingreso ingreso = construirDesdeRequest(new Ingreso(), request);
        return convertirAResponse(ingresoRepository.save(ingreso));
    }

    @Override
    public IngresoResponse actualizar(Integer id, IngresoRequest request) {
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ingreso no encontrado con id: " + id));
        construirDesdeRequest(ingreso, request);
        return convertirAResponse(ingresoRepository.save(ingreso));
    }

    @Override
    public void eliminar(Integer id) {
        if (!ingresoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Ingreso no encontrado con id: " + id);
        }
        ingresoRepository.deleteById(id);
    }

    // Rellena los campos del ingreso con los datos del request, resolviendo las relaciones
    private Ingreso construirDesdeRequest(Ingreso ingreso, IngresoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + request.getUsuarioId()));
        ingreso.setUsuario(usuario);
        ingreso.setMonto(request.getMonto());
        ingreso.setFecha(request.getFecha());
        ingreso.setEsActiva(request.isEsActiva());
        ingreso.setFuenteIngreso(request.getFuenteIngreso());

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + request.getCategoriaId()));
            ingreso.setCategoria(categoria);
        }

        if (request.getPresupuestoId() != null) {
            Presupuesto presupuesto = presupuestoRepository.findById(request.getPresupuestoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Presupuesto no encontrado con id: " + request.getPresupuestoId()));
            ingreso.setPresupuesto(presupuesto);
        }

        return ingreso;
    }

    private IngresoResponse convertirAResponse(Ingreso ingreso) {
        IngresoResponse response = new IngresoResponse();
        response.setId(ingreso.getId());
        response.setMonto(ingreso.getMonto());
        response.setFecha(ingreso.getFecha());
        response.setEsActiva(ingreso.isEsActiva());
        response.setFuenteIngreso(ingreso.getFuenteIngreso());
        response.setUsuarioId(ingreso.getUsuario() != null ? ingreso.getUsuario().getId() : null);
        response.setPresupuestoId(ingreso.getPresupuesto() != null ? ingreso.getPresupuesto().getId() : null);

        if (ingreso.getCategoria() != null) {
            IngresoResponse.CategoriaResumen cat = new IngresoResponse.CategoriaResumen();
            cat.setId(ingreso.getCategoria().getId());
            cat.setNombre(ingreso.getCategoria().getNombre());
            response.setCategoria(cat);
        }

        return response;
    }
}
