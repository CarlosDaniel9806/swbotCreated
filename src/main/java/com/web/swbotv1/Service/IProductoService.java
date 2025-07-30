package com.web.swbotv1.Service;

import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Dto.ProductoDto;
import com.web.swbotv1.commons.IBaseInterfaceService;
import com.web.swbotv1.model.Producto;



public interface IProductoService extends IBaseInterfaceService <Producto, Long> {
   
    ProductoDto createProducto(ProductoDto dto); 
    ProductoDto subirImagenes(Long idProducto, MultipartFile[] imagenes);
    
}

