package com.finanzas.controlgastos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Categoría para clasificar transacciones; soporta árbol de subcategorías mediante auto-referencia
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    // Indica si la categoría es global (visible para todos) o personal
    @Column(name = "es_global")
    private boolean esGlobal;

    // Categoría padre; null indica que es una categoría raíz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Categoria padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categoria> subcategorias = new ArrayList<>();

    public Categoria() {}

    public Categoria(String nombre, boolean esGlobal) {
        this.nombre = nombre;
        this.esGlobal = esGlobal;
    }

    // Agrega una subcategoría manteniendo la consistencia bidireccional
    public void agregarSubcategoria(Categoria subcategoria) {
        subcategorias.add(subcategoria);
        subcategoria.setPadre(this);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isEsGlobal() { return esGlobal; }
    public void setEsGlobal(boolean esGlobal) { this.esGlobal = esGlobal; }

    public Categoria getPadre() { return padre; }
    public void setPadre(Categoria padre) { this.padre = padre; }

    public List<Categoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<Categoria> subcategorias) { this.subcategorias = subcategorias; }
}
