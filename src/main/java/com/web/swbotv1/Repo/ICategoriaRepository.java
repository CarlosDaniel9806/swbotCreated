package com.web.swbotv1.Repo;

import java.util.Optional;

import com.web.swbotv1.model.Categoria;


public interface ICategoriaRepository extends IGenericRepository<Categoria, Long> { /* mi interface personalizada para Producto con los metodos de IgenericRepository y JpaRepository Save, findById tc */
    
    // Aquí puedes agregar métodos específicos para Producto si es necesario
    // Por ejemplo:
    // List<Producto> findByNombre(String nombre);

    Optional<Categoria> findByNombreCategoriaIgnoreCase(String nombreCategoria); // sirve para buscar una categoria por su nombre y ver si existe

    
}
