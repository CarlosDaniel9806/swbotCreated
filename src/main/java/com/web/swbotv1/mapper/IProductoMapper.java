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
    
 

    @Mapping(source = "categoria.nombreCategoria", target = "nombreCategoria")
    ProductoDto toDto(Producto producto);

    
    @InheritInverseConfiguration
    @Mapping(target = "categoria", ignore = true) // se asignará manualmente en el servicio
    Producto toEntity(ProductoDto producto);

    List<ProductoDto> toDtoList(List<Producto> productos);
}

