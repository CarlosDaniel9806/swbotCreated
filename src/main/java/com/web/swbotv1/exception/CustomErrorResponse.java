package com.web.swbotv1.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse { // sirve para manejar los errores

    private LocalDateTime datetime;
    private String message;
    private String details;
    
}
