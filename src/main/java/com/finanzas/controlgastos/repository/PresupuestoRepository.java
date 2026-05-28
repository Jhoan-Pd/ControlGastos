package com.finanzas.controlgastos.repository;

import com.finanzas.controlgastos.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Integer> {

    List<Presupuesto> findByUsuarioId(Integer usuarioId);
}
