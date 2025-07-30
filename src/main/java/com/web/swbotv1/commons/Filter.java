package com.web.swbotv1.commons;

/* Representa un solo criterio de filtrado (un campo, un valor y un tipo de comparación) */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Filter {

    private String field;
    private String value;
    private String typeComparation;

    
}
