package com.finanzas.controlgastos.repository;

import com.finanzas.controlgastos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Retorna solo las categorías raíz (sin padre)
    List<Categoria> findByPadreIsNull();

    List<Categoria> findByEsGlobal(boolean esGlobal);
}
