package com.web.swbotv1.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.web.swbotv1.model.Producto;

public interface IProductoRepository extends IGenericRepository<Producto, Long> { /* mi interface personalizada para Producto con los metodos de IgenericRepository y JpaRepository Save, findById tc */
    
    // Si algún día quieres validar productos duplicados:
    Optional<Producto> findByNombreProductoIgnoreCase(String nombreProducto);
    
    
}
