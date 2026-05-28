package com.finanzas.controlgastos.dto.response;

import java.util.List;

// DTO de salida para exponer una categoría con su árbol de subcategorías
public class CategoriaResponse {

    private Integer id;
    private String nombre;
    private boolean esGlobal;
    // Subcategorías anidadas para que el frontend pueda renderizar la jerarquía
    private List<CategoriaResponse> subcategorias;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isEsGlobal() { return esGlobal; }
    public void setEsGlobal(boolean esGlobal) { this.esGlobal = esGlobal; }

    public List<CategoriaResponse> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<CategoriaResponse> subcategorias) { this.subcategorias = subcategorias; }
}
