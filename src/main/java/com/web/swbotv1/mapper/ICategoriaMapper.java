package com.web.swbotv1.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.web.swbotv1.Dto.CategoriaDto;
import com.web.swbotv1.model.Categoria;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) /* poder usar el @Autowired Haz que este mapper funcione como un componente de Spring  */

public interface ICategoriaMapper {
    
   
    CategoriaDto toDto(Categoria categoria);

    @InheritInverseConfiguration
    Categoria toEntity(CategoriaDto categoria);

    List<CategoriaDto> toDtoList(List<Categoria> categorias);
}

