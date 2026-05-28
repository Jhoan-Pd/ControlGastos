package com.finanzas.controlgastos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Presupuesto asignado a un usuario para controlar gastos en un período
@Entity
@Table(name = "presupuestos")
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    @Column(name = "saldo_disponible", precision = 15, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Transacciones asociadas a este presupuesto
    @OneToMany(mappedBy = "presupuesto")
    private List<Transaccion> transacciones = new ArrayList<>();

    public Presupuesto() {}

    public Presupuesto(String descripcion, BigDecimal saldoDisponible,
                       LocalDate fechaInicio, LocalDate fechaFin, Usuario usuario) {
        this.descripcion = descripcion;
        this.saldoDisponible = saldoDisponible;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuario = usuario;
    }

    // Agrega una transacción al presupuesto y establece la referencia inversa
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
        transaccion.setPresupuesto(this);
    }

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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Transaccion> getTransacciones() { return transacciones; }
    public void setTransacciones(List<Transaccion> transacciones) { this.transacciones = transacciones; }
}
