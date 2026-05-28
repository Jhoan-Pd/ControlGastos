package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.GastoRequest;
import com.finanzas.controlgastos.dto.response.GastoResponse;
import com.finanzas.controlgastos.exception.RecursoNoEncontradoException;
import com.finanzas.controlgastos.model.Categoria;
import com.finanzas.controlgastos.model.Gasto;
import com.finanzas.controlgastos.model.Presupuesto;
import com.finanzas.controlgastos.model.Usuario;
import com.finanzas.controlgastos.repository.CategoriaRepository;
import com.finanzas.controlgastos.repository.GastoRepository;
import com.finanzas.controlgastos.repository.PresupuestoRepository;
import com.finanzas.controlgastos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GastoService implements IGastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Override
    public List<GastoResponse> obtenerTodos() {
        return gastoRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GastoResponse> obtenerPorId(Integer id) {
        return gastoRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public List<GastoResponse> obtenerPorUsuario(Integer usuarioId) {
        return gastoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GastoResponse guardar(GastoRequest request) {
        Gasto gasto = construirDesdeRequest(new Gasto(), request);
        return convertirAResponse(gastoRepository.save(gasto));
    }

    @Override
    public GastoResponse actualizar(Integer id, GastoRequest request) {
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Gasto no encontrado con id: " + id));
        construirDesdeRequest(gasto, request);
        return convertirAResponse(gastoRepository.save(gasto));
    }

    @Override
    public void eliminar(Integer id) {
        if (!gastoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Gasto no encontrado con id: " + id);
        }
        gastoRepository.deleteById(id);
    }

    // Rellena los campos del gasto con los datos del request, resolviendo las relaciones
    private Gasto construirDesdeRequest(Gasto gasto, GastoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + request.getUsuarioId()));
        gasto.setUsuario(usuario);
        gasto.setMonto(request.getMonto());
        gasto.setFecha(request.getFecha());
        gasto.setEsActiva(request.isEsActiva());
        gasto.setEsFijo(request.isEsFijo());

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + request.getCategoriaId()));
            gasto.setCategoria(categoria);
        }

        if (request.getPresupuestoId() != null) {
            Presupuesto presupuesto = presupuestoRepository.findById(request.getPresupuestoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Presupuesto no encontrado con id: " + request.getPresupuestoId()));
            gasto.setPresupuesto(presupuesto);
        }

        return gasto;
    }

    private GastoResponse convertirAResponse(Gasto gasto) {
        GastoResponse response = new GastoResponse();
        response.setId(gasto.getId());
        response.setMonto(gasto.getMonto());
        response.setFecha(gasto.getFecha());
        response.setEsActiva(gasto.isEsActiva());
        response.setEsFijo(gasto.isEsFijo());
        response.setUsuarioId(gasto.getUsuario() != null ? gasto.getUsuario().getId() : null);
        response.setPresupuestoId(gasto.getPresupuesto() != null ? gasto.getPresupuesto().getId() : null);

        if (gasto.getCategoria() != null) {
            GastoResponse.CategoriaResumen cat = new GastoResponse.CategoriaResumen();
            cat.setId(gasto.getCategoria().getId());
            cat.setNombre(gasto.getCategoria().getNombre());
            response.setCategoria(cat);
        }

        return response;
    }
}
