package com.web.swbotv1.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.web.swbotv1.Dto.ProductoDto;
import com.web.swbotv1.Service.IProductoService;
import com.web.swbotv1.mapper.IProductoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // URL de tu frontend React
@RequestMapping("/api/productos") // Define la ruta base para este controlador

public class ProductoController {

    private final IProductoService service;
    private final IProductoMapper productoMapper;

    // Crear producto (sin imágenes)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoDto> crearProducto(@RequestBody ProductoDto dto) {
        ProductoDto creado = service.createProducto(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    // Subir imágenes
    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDto> subirImagenes(
            @PathVariable("id") Long idProducto,
            @RequestParam("imagenes") MultipartFile[] imagenes
    ) {

        if (imagenes == null || imagenes.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        ProductoDto actualizado = service.subirImagenes(idProducto, imagenes);
        return ResponseEntity.ok(actualizado);
    }

    // Obtener todos los productos
    @GetMapping("/all")
    public ResponseEntity<List<ProductoDto>> getAllProductos() {

        var productos = service.getAll();

        var responseDtos = productos.stream()
            .map(producto -> {
                ProductoDto dto = productoMapper.toDto(producto);

                List<String> urls = (producto.getImagenes() != null)
                    ? producto.getImagenes().stream()
                        .map(nombre -> "/uploads/" + nombre)
                        .collect(Collectors.toList())
                    : List.of();

                dto.setImagenesUrl(urls);
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

}


