package com.web.swbotv1.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BussinessRuleException extends RuntimeException  { // sirve para manejar las reglas de negocio

    private long id;
    private String code;
    private HttpStatus httpStatus;

    public BussinessRuleException(long id, String code, String message, HttpStatus httpStatus){ //sirve para manejar las reglas de negocio
        super(message);
        this.id = id;
        this.code = code;
        this.httpStatus = httpStatus; 
    }

    public BussinessRuleException(String code, String message, HttpStatus httpStatus){
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BussinessRuleException(String message, Throwable throwable){
        super(message, throwable);
    }
    
}
