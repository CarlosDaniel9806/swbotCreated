package com.web.swbotv1.commons;

/* es una clase que agrupa una lista de varios Filter, permitiendo aplicar múltiples criterios de filtrado al mismo tiempo. */


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Filters {

    private List<Filter> filters;
    
}
