package com.finanzas.controlgastos.config;

import com.finanzas.controlgastos.model.Categoria;
import com.finanzas.controlgastos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Inserta categorías globales al iniciar si la tabla está vacía
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) return;

        crearCategoria("Alimentación", true,
                List.of("Supermercado", "Restaurantes", "Cafetería"));

        crearCategoria("Transporte", true,
                List.of("Combustible", "Transporte público", "Taxi / App"));

        crearCategoria("Vivienda", true,
                List.of("Arriendo", "Servicios públicos", "Mantenimiento"));

        crearCategoria("Salud", true,
                List.of("Médico", "Medicamentos", "Gimnasio"));

        crearCategoria("Educación", true,
                List.of("Matrícula", "Libros", "Cursos"));

        crearCategoria("Entretenimiento", true,
                List.of("Streaming", "Salidas", "Hobbies"));

        crearCategoria("Ropa y calzado", true, List.of());

        crearCategoria("Ingresos laborales", true,
                List.of("Salario", "Horas extra", "Bonificaciones"));

        crearCategoria("Ingresos adicionales", true,
                List.of("Freelance", "Arriendo", "Inversiones"));

        crearCategoria("Otros", true, List.of());
    }

    private void crearCategoria(String nombre, boolean esGlobal, List<String> subcategorias) {
        Categoria padre = new Categoria(nombre, esGlobal);
        categoriaRepository.save(padre);
        for (String sub : subcategorias) {
            padre.agregarSubcategoria(new Categoria(sub, esGlobal));
        }
        categoriaRepository.save(padre);
    }
}
