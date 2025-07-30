package com.web.swbotv1.Service;

import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Dto.CategoriaDto;
import com.web.swbotv1.commons.IBaseInterfaceService;
import com.web.swbotv1.model.Categoria;


public interface ICategoriaService extends IBaseInterfaceService <Categoria, Long> {
    
    Categoria createCustom (CategoriaDto entidad, MultipartFile imagen); // este es la firma del metodo createCustom que se implementa en la clase CategoriaServiceImpl
    
    
    

   
}
