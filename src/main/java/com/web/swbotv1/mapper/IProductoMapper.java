package com.web.swbotv1.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.web.swbotv1.Dto.ProductoDto;
import com.web.swbotv1.model.Producto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) /* poder usar el @Autowired Haz que este mapper funcione como un componente de Spring  */

public interface IProductoMapper {
    
    
    @Mapping(source = "imagenes", target = "imagenesUrl")
    ProductoDto toDto(Producto producto);

    @InheritInverseConfiguration
    @Mapping(target = "imagenes", source = "imagenesUrl")
    Producto toEntity(ProductoDto productoDto);

    List<ProductoDto> toDtoList(List<Producto> productos);
}

