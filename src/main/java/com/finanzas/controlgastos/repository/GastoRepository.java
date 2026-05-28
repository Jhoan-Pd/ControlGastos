package com.finanzas.controlgastos.repository;

import com.finanzas.controlgastos.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {

    List<Gasto> findByUsuarioId(Integer usuarioId);

    List<Gasto> findByPresupuestoId(Integer presupuestoId);

    List<Gasto> findByUsuarioIdAndEsFijo(Integer usuarioId, boolean esFijo);
}
