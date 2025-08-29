package com.web.swbotv1.Service.impl;


import java.io.IOException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Dto.CategoriaDto;
import com.web.swbotv1.Repo.ICategoriaRepository;
import com.web.swbotv1.Repo.IGenericRepository;
import com.web.swbotv1.Service.ICategoriaService;
import com.web.swbotv1.Service.IUploadFileService;
import com.web.swbotv1.exception.BussinessRuleException;
import com.web.swbotv1.mapper.ICategoriaMapper;
import com.web.swbotv1.model.Categoria;

import lombok.RequiredArgsConstructor;


@Service
@Transactional  /* Si todo el método se ejecuta correctamente, los cambios en la base de datos se confirman (commit).
                   Si ocurre una excepción, todos los cambios se revierten (rollback), evitando datos inconsistentes. */
@RequiredArgsConstructor
public class CategoriaServiceImpl extends CRUDimpl<Categoria, Long>  implements ICategoriaService {

    private final IGenericRepository<Categoria, Long> repository; //esta linea es para inyectar el repositorio de Categoria a la clase padre CRUDimpl 

    //Mapeo de Dto a Entity y viceversa
    private final ICategoriaMapper iCategoriaMapper; //Esta línea te permite usar el mapper para transformar objetos dentro de tu servicio
    private final ICategoriaRepository iCategoriaRepository; // Inyectamos el servicio de Categoria para poder obtener la categoria asociada al categoria

    // Pedimos al repositorio el Categoria getRepo
    @Override                                                   //esta linea es para sobreescribir el metodo de la clase padre CRUDimpl
    protected IGenericRepository<Categoria, Long> getRepo() {    // esta linea sirve para obtener el repositorio de Categoria y tambien para inyectarlo en la interface IGenericRepository 
        return repository;                                      //retorna el repositorio de Categoria
    }

    
    private final IUploadFileService uploadFileService;
    

    /* ........................Metodo De createCustom o Crear Categoria con Imagen ......................... */

    @Override
public Categoria createCustom(CategoriaDto dto, MultipartFile imagen) {
    // 1. Validar si la categoría ya existe
    Optional<Categoria> existente = iCategoriaRepository.findByNombreCategoriaIgnoreCase(dto.getNombreCategoria().trim());
    if (existente.isPresent()) {
        throw new BussinessRuleException(
            "CATEGORIA_DUPLICADA",
            "La categoría '" + dto.getNombreCategoria().trim() + "' ya existe.",
            HttpStatus.CONFLICT
        );
    }

    // 2. Subir imagen si se proporciona
    String nombreImagen = null;
    if (imagen != null && !imagen.isEmpty()) {
        try {
            nombreImagen = uploadFileService.copy(imagen);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage(), e);
        }
    }

    // 3. Mapear y guardar
    Categoria categoria = iCategoriaMapper.toEntity(dto);
    categoria.setNombreCategoria(dto.getNombreCategoria().trim());

    // 4. Asignar URL de la imagen si se subió
    if (nombreImagen != null) {
        categoria.setImagenUrl("/uploads/" + nombreImagen);
    }

    // 5. Guardar y retornar
    return super.create(categoria);
}

/* ........................Metodo De Update o Editar Categoria......................... */

  @Override
public Categoria updateCustom(Long id, CategoriaDto dto, MultipartFile imagen) {
    // 1. Verificar que la categoría existe
    Categoria categoriaExistente = super.readById(id);
    
    // 2. Validar si el nuevo nombre ya existe (solo si cambió)
    if (!categoriaExistente.getNombreCategoria().equalsIgnoreCase(dto.getNombreCategoria().trim())) {
        Optional<Categoria> existente = iCategoriaRepository.findByNombreCategoriaIgnoreCase(dto.getNombreCategoria().trim());
        if (existente.isPresent()) {
            throw new BussinessRuleException(
                "CATEGORIA_DUPLICADA",
                "La categoría '" + dto.getNombreCategoria().trim() + "' ya existe.",
                HttpStatus.CONFLICT
            );
        }
    }

    // 3. Actualizar nombre
    categoriaExistente.setNombreCategoria(dto.getNombreCategoria().trim());

    // 4. Actualizar imagen si se proporciona una nueva
    if (imagen != null && !imagen.isEmpty()) {
        try {
            String nombreImagen = uploadFileService.copy(imagen);
            categoriaExistente.setImagenUrl("/uploads/" + nombreImagen);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage(), e);
        }
    }

    // 5. Guardar y retornar
    return super.update(categoriaExistente, id);
}
}
