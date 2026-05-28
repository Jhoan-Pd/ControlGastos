package com.finanzas.controlgastos.service;

import com.finanzas.controlgastos.dto.request.CategoriaRequest;
import com.finanzas.controlgastos.dto.response.CategoriaResponse;
import com.finanzas.controlgastos.exception.RecursoNoEncontradoException;
import com.finanzas.controlgastos.model.Categoria;
import com.finanzas.controlgastos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaService implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaResponse> obtenerTodasRaiz() {
        // Solo devuelve categorías raíz; las subcategorías están anidadas dentro de cada una
        return categoriaRepository.findByPadreIsNull()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoriaResponse> obtenerPorId(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    public CategoriaResponse guardar(CategoriaRequest request) {
        Categoria categoria = new Categoria(request.getNombre(), request.isEsGlobal());

        if (request.getPadreId() != null) {
            Categoria padre = categoriaRepository.findById(request.getPadreId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría padre no encontrada con id: " + request.getPadreId()));
            padre.agregarSubcategoria(categoria);
        }

        return convertirAResponse(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponse actualizar(Integer id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + id));
        categoria.setNombre(request.getNombre());
        categoria.setEsGlobal(request.isEsGlobal());
        return convertirAResponse(categoriaRepository.save(categoria));
    }

    @Override
    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    // Convierte recursivamente la categoría y sus subcategorías al DTO de respuesta
    private CategoriaResponse convertirAResponse(Categoria categoria) {
        CategoriaResponse response = new CategoriaResponse();
        response.setId(categoria.getId());
        response.setNombre(categoria.getNombre());
        response.setEsGlobal(categoria.isEsGlobal());
        response.setSubcategorias(
            categoria.getSubcategorias()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList())
        );
        return response;
    }
}
