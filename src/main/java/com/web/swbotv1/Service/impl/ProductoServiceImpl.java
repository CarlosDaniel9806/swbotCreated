package com.web.swbotv1.Service.impl;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.web.swbotv1.Dto.ProductoDto;
import com.web.swbotv1.Repo.IGenericRepository;
import com.web.swbotv1.Repo.IProductoRepository;
import com.web.swbotv1.Service.IProductoService;
import com.web.swbotv1.Service.IUploadFileService;
import com.web.swbotv1.exception.BussinessRuleException;
import com.web.swbotv1.mapper.IProductoMapper;
import com.web.swbotv1.model.Producto;
import lombok.RequiredArgsConstructor;


@Service
@Transactional  /* Si todo el método se ejecuta correctamente, los cambios en la base de datos se confirman (commit).
                Si ocurre una excepción, todos los cambios se revierten (rollback), evitando datos inconsistentes. */
@RequiredArgsConstructor
public class ProductoServiceImpl extends CRUDimpl<Producto, Long>  implements IProductoService {

private final IProductoRepository productoRepository;
    private final IUploadFileService uploadFileService;
    private final IProductoMapper productoMapper;

    @Override
    protected IGenericRepository<Producto, Long> getRepo() {
        return productoRepository;
    }

    // Crear producto SIN categorías
    @Override
    public ProductoDto createProducto(ProductoDto dto) {

        if (productoRepository.findByNombreProductoIgnoreCase(dto.getNombreProducto().trim()).isPresent()) {
            throw new BussinessRuleException(
                "PRODUCTO_DUPLICADO",
                "El producto '" + dto.getNombreProducto() + "' ya existe.",
                HttpStatus.CONFLICT
            );
        }

        Producto entity = productoMapper.toEntity(dto);
        Producto guardado = productoRepository.save(entity);

        return productoMapper.toDto(guardado);
    }

    // Subir múltiples imágenes
    @Override
    public ProductoDto subirImagenes(Long idProducto, MultipartFile[] imagenes) {

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new BussinessRuleException(
                        "PRODUCTO_NO_ENCONTRADO",
                        "Producto no encontrado",
                        HttpStatus.NOT_FOUND));

        List<String> rutasGuardadas;
        try {
            rutasGuardadas = uploadFileService.copyMultiple(imagenes);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imágenes: " + e.getMessage());
        }

        producto.setImagenes(rutasGuardadas);
        producto = productoRepository.save(producto);

        ProductoDto dto = productoMapper.toDto(producto);

        List<String> urls = producto.getImagenes().stream()
                .map(nombre -> "/uploads/" + nombre)
                .collect(Collectors.toList());

        dto.setImagenesUrl(urls);

        return dto;
    }
}




