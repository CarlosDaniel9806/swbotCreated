package com.web.swbotv1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Dto.CategoriaDto;
import com.web.swbotv1.Service.ICategoriaService;
import com.web.swbotv1.mapper.ICategoriaMapper;
import com.web.swbotv1.model.Categoria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // URL de tu frontend React
@RequestMapping("/api/categorias") // Define la ruta base para este controlador

public class CategoriaController {
    
    
    
    private final ICategoriaService categoriaService;
    private final ICategoriaMapper iCategoriaMapper;


    /* ........................EndPoint De Update o Editar Categoria......................... */
    
   @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<CategoriaDto> crearCategoria(
    @RequestParam("nombreCategoria") String nombreCategoria,
    @RequestParam(value = "imagen", required = false) MultipartFile imagen
) {
    CategoriaDto dto = new CategoriaDto();
    dto.setNombreCategoria(nombreCategoria);

    Categoria categoria = categoriaService.createCustom(dto, imagen);
    CategoriaDto dtoRespuesta = iCategoriaMapper.toDto(categoria);
    return new ResponseEntity<>(dtoRespuesta, HttpStatus.CREATED);
}

    // 🧾 Obtener todas las categorías existentes
     @GetMapping("/all")
    public ResponseEntity<List<CategoriaDto>> getAllCategorias() {
        var categorias = categoriaService.getAll();
        var responseDtos = categorias.stream()
                                     .map(iCategoriaMapper::toDto)
                                     .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }


    /* ........................EndPoint De Update o Editar Categoria......................... */
@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<CategoriaDto> actualizarCategoria(
    @PathVariable Long id,
    @RequestParam("nombreCategoria") String nombreCategoria,
    @RequestParam(value = "imagen", required = false) MultipartFile imagen
) {
    CategoriaDto dto = new CategoriaDto();
    dto.setNombreCategoria(nombreCategoria);

    Categoria categoria = categoriaService.updateCustom(id, dto, imagen);
    CategoriaDto dtoRespuesta = iCategoriaMapper.toDto(categoria);
    return ResponseEntity.ok(dtoRespuesta);
}
}
