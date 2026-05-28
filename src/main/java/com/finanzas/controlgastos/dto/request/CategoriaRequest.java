package com.finanzas.controlgastos.dto.request;

// DTO de entrada para crear o actualizar una categoría
public class CategoriaRequest {

    private String nombre;
    private boolean esGlobal;
    // ID de la categoría padre; null indica que es una categoría raíz
    private Integer padreId;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isEsGlobal() { return esGlobal; }
    public void setEsGlobal(boolean esGlobal) { this.esGlobal = esGlobal; }

    public Integer getPadreId() { return padreId; }
    public void setPadreId(Integer padreId) { this.padreId = padreId; }
}
