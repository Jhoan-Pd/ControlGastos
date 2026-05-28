package com.finanzas.controlgastos.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de entrada para registrar o actualizar un ingreso
public class IngresoRequest {

    private Integer usuarioId;
    private Integer categoriaId;
    private Integer presupuestoId;
    private BigDecimal monto;
    private LocalDate fecha;
    private boolean esActiva = true;
    private String fuenteIngreso;

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public Integer getPresupuestoId() { return presupuestoId; }
    public void setPresupuestoId(Integer presupuestoId) { this.presupuestoId = presupuestoId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isEsActiva() { return esActiva; }
    public void setEsActiva(boolean esActiva) { this.esActiva = esActiva; }

    public String getFuenteIngreso() { return fuenteIngreso; }
    public void setFuenteIngreso(String fuenteIngreso) { this.fuenteIngreso = fuenteIngreso; }
}
