package com.finanzas.controlgastos.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de entrada para crear o actualizar un presupuesto
public class PresupuestoRequest {

    private Integer usuarioId;
    private String descripcion;
    private BigDecimal saldoDisponible;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
}
