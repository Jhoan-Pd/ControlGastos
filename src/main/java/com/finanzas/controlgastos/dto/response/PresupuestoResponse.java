package com.finanzas.controlgastos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de salida para exponer los datos de un presupuesto
public class PresupuestoResponse {

    private Integer id;
    private String descripcion;
    private BigDecimal saldoDisponible;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer usuarioId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
}
