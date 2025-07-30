package com.web.swbotv1.Dto;


import java.io.Serializable;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EqualsAndHashCode.Include
    private Long idCategoria;

    @NotBlank(message = "no puede estar vacia")
    private String nombreCategoria;
    
       
    // 🟢 Este es para devolver la URL de la imagen al frontend
    private String imagenUrl;
        
}
    

