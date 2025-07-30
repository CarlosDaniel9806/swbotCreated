package com.web.swbotv1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException { // sirve para crear una excepcion personalizada

    public  BadRequestException(String message) {  
        super(message);
    }
    
}
