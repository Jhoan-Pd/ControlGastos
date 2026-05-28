package com.finanzas.controlgastos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Transacción de entrada de dinero; hereda de Transaccion mediante estrategia JOINED
@Entity
@Table(name = "ingresos")
public class Ingreso extends Transaccion {

    // Origen o fuente del ingreso (salario, freelance, inversión, etc.)
    @Column(name = "fuente_ingreso")
    private String fuenteIngreso;

    public Ingreso() {
        super();
    }

    public Ingreso(BigDecimal monto, LocalDate fecha, boolean esActiva,
                   Usuario usuario, Categoria categoria, String fuenteIngreso) {
        super(monto, fecha, esActiva, usuario, categoria);
        this.fuenteIngreso = fuenteIngreso;
    }

    public String getFuenteIngreso() { return fuenteIngreso; }
    public void setFuenteIngreso(String fuenteIngreso) { this.fuenteIngreso = fuenteIngreso; }
}
