package com.finanzas.controlgastos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Clase base abstracta de todas las transacciones; usa herencia JOINED para respetar la jerarquía POO
@Entity
@Table(name = "transacciones")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "es_activa")
    private boolean esActiva = true;

    // Relación con el usuario dueño de la transacción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Categoría opcional para clasificar la transacción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Presupuesto al que pertenece esta transacción (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presupuesto_id")
    private Presupuesto presupuesto;

    public Transaccion() {}

    public Transaccion(BigDecimal monto, LocalDate fecha, boolean esActiva,
                       Usuario usuario, Categoria categoria) {
        this.monto = monto;
        this.fecha = fecha;
        this.esActiva = esActiva;
        this.usuario = usuario;
        this.categoria = categoria;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isEsActiva() { return esActiva; }
    public void setEsActiva(boolean esActiva) { this.esActiva = esActiva; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Presupuesto getPresupuesto() { return presupuesto; }
    public void setPresupuesto(Presupuesto presupuesto) { this.presupuesto = presupuesto; }
}
