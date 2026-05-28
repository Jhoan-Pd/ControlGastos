package com.finanzas.controlgastos.repository;

import com.finanzas.controlgastos.model.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

    List<Ingreso> findByUsuarioId(Integer usuarioId);

    List<Ingreso> findByPresupuestoId(Integer presupuestoId);
}
