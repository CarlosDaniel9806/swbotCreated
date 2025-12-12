package com.web.swbotv1.Dto;


import java.io.Serializable;
import java.util.List;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long idProducto;

    @NotBlank(message = "no puede estar vacia")
    private String nombreProducto;
    
    // 🔥 🟢 Este es para devolver la URL de la imagen al frontend
    private List<String> imagenesUrl;
}
    

