package com.finanzas.controlgastos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Transacción de egreso; hereda de Transaccion mediante estrategia JOINED
@Entity
@Table(name = "gastos")
public class Gasto extends Transaccion {

    // Indica si el gasto es recurrente (fijo) o esporádico (variable)
    @Column(name = "es_fijo")
    private boolean esFijo;

    public Gasto() {
        super();
    }

    public Gasto(BigDecimal monto, LocalDate fecha, boolean esActiva,
                 Usuario usuario, Categoria categoria, boolean esFijo) {
        super(monto, fecha, esActiva, usuario, categoria);
        this.esFijo = esFijo;
    }

    public boolean isEsFijo() { return esFijo; }
    public void setEsFijo(boolean esFijo) { this.esFijo = esFijo; }
}
