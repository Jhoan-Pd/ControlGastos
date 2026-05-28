package com.finanzas.controlgastos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de salida para exponer los datos de un gasto; incluye categoría anidada para el frontend
public class GastoResponse {

    private Integer id;
    private BigDecimal monto;
    private LocalDate fecha;
    private boolean esActiva;
    private boolean esFijo;
    private Integer usuarioId;
    private Integer presupuestoId;
    // Objeto anidado para que el frontend pueda acceder a categoria.nombre
    private CategoriaResumen categoria;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isEsActiva() { return esActiva; }
    public void setEsActiva(boolean esActiva) { this.esActiva = esActiva; }

    public boolean isEsFijo() { return esFijo; }
    public void setEsFijo(boolean esFijo) { this.esFijo = esFijo; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getPresupuestoId() { return presupuestoId; }
    public void setPresupuestoId(Integer presupuestoId) { this.presupuestoId = presupuestoId; }

    public CategoriaResumen getCategoria() { return categoria; }
    public void setCategoria(CategoriaResumen categoria) { this.categoria = categoria; }

    // Resumen mínimo de categoría para evitar exponer la entidad completa
    public static class CategoriaResumen {
        private Integer id;
        private String nombre;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}
