package com.web.swbotv1.Service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Dto.ProductoDto;
import com.web.swbotv1.Repo.ICategoriaRepository;
import com.web.swbotv1.Repo.IGenericRepository;
import com.web.swbotv1.Repo.IProductoRepository;
import com.web.swbotv1.Service.IProductoService;
import com.web.swbotv1.Service.IUploadFileService;
import com.web.swbotv1.exception.BussinessRuleException;
import com.web.swbotv1.mapper.IProductoMapper;
import com.web.swbotv1.model.Categoria;
import com.web.swbotv1.model.Producto;

import lombok.RequiredArgsConstructor;





@Service
@Transactional  /* Si todo el método se ejecuta correctamente, los cambios en la base de datos se confirman (commit).
                Si ocurre una excepción, todos los cambios se revierten (rollback), evitando datos inconsistentes. */
@RequiredArgsConstructor
public class ProductoServiceImpl extends CRUDimpl<Producto, Long>  implements IProductoService {

//Inizializar
private final IGenericRepository<Producto, Long> repository; //esta linea es para inyectar el repositorio de Producto a la clase padre CRUDimpl 

private final ICategoriaRepository categoriaRepository;
private final IProductoRepository iProductorepository;
private final IUploadFileService uploadFileService;


//Mapeo de Dto a Entity y viceversa
private final IProductoMapper iProductoMapper; //Esta línea te permite usar el mapper para transformar objetos dentro de tu servicio


// Pedimos al repositorio el Producto getRepo
@Override                                                   //esta linea es para sobreescribir el metodo de la clase padre CRUDimpl
protected IGenericRepository<Producto, Long> getRepo() {    // esta linea sirve para obtener el repositorio de Producto y tambien para inyectarlo en la interface IGenericRepository 
    return repository;                                      //retorna el repositorio de Producto
}

@Override
public ProductoDto createProducto(ProductoDto dto) {
    // 1. Buscar o crear categoría
    Optional<Categoria> categoriaOpt = categoriaRepository.findByNombreCategoriaIgnoreCase(dto.getNombreCategoria().trim());

    Categoria categoria = categoriaOpt.orElseGet(() -> {
        Categoria nueva = new Categoria();
        nueva.setNombreCategoria(dto.getNombreCategoria().trim());
        return categoriaRepository.save(nueva);
    });

    // 2. Validar producto duplicado
    Optional<Producto> productoExistente = iProductorepository
        .findByNombreProductoIgnoreCaseAndCategoriaNombreCategoriaIgnoreCase(
            dto.getNombreProducto().trim(), dto.getNombreCategoria().trim());

    if (productoExistente.isPresent()) {
        throw new BussinessRuleException(
            "PRODUCTO_DUPLICADO",
            "El producto '" + dto.getNombreProducto() + "' ya existe en la categoría '" + dto.getNombreCategoria() + "'.",
            HttpStatus.CONFLICT
        );
    }

    // 3. Crear entidad y guardar
    Producto producto = iProductoMapper.toEntity(dto);
    producto.setCategoria(categoria);

    Producto productoGuardado = super.create(producto);
    return iProductoMapper.toDto(productoGuardado);
}

@Override
public ProductoDto subirImagenes(Long idProducto, MultipartFile[] imagenes) {
    Producto producto = iProductorepository.findById(idProducto)
        .orElseThrow(() -> new BussinessRuleException("PRODUCTO_NO_ENCONTRADO", "Producto no encontrado", HttpStatus.NOT_FOUND));

    List<String> rutas = new ArrayList<>();
    for (MultipartFile imagen : imagenes) {
        if (!imagen.isEmpty()) {
            try {
                String nombreImagen = uploadFileService.copy(imagen);
                rutas.add(nombreImagen);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen: " + e.getMessage(), e);
            }
        }
    }

    producto.setImagenes(rutas);
    Producto productoActualizado = iProductorepository.save(producto);

    ProductoDto dtoRespuesta = iProductoMapper.toDto(productoActualizado);
    List<String> urls = productoActualizado.getImagenes().stream()
        .map(nombre -> "/uploads/" + nombre)
        .collect(Collectors.toList());
    dtoRespuesta.setImagenesUrl(urls);

    return dtoRespuesta;
}
}


