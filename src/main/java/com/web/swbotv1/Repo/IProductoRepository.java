package com.web.swbotv1.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.web.swbotv1.model.Producto;

public interface IProductoRepository extends IGenericRepository<Producto, Long> { /* mi interface personalizada para Producto con los metodos de IgenericRepository y JpaRepository Save, findById tc */
    
    // Aquí puedes agregar métodos específicos para Producto si es necesario
    // Por ejemplo:
    // List<Producto> findByNombre(String nombre);
    Optional<Producto> findByNombreProductoIgnoreCaseAndCategoriaNombreCategoriaIgnoreCase(String nombreProducto, String nombreCategoria);

     // Nuevo método para contar productos por categoría
    @Query("SELECT p.categoria.idCategoria, COUNT(p) " +
           "FROM Producto p " +
           "GROUP BY p.categoria.idCategoria")
    List<Object[]> countProductosPorCategoria();
    
}
