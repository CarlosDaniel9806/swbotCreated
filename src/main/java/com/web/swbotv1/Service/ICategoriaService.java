package com.web.swbotv1.Service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Dto.CategoriaDto;
import com.web.swbotv1.commons.IBaseInterfaceService;
import com.web.swbotv1.model.Categoria;


public interface ICategoriaService extends IBaseInterfaceService <Categoria, Long> {
    
    Categoria createCustom (CategoriaDto entidad, MultipartFile imagen); // este es la firma del metodo createCustom que se implementa en la clase CategoriaServiceImpl
    
    Categoria updateCustom(Long id, CategoriaDto dto, MultipartFile imagen); // este es la firma para el metodo updateCustom que se implementa en la clase CategoriaServiceImpl
    
    // Para el conteo de Prductos 
    Map<Long, Integer> countProductosPorCategoria();
}
