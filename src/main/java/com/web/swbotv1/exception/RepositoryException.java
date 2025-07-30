package com.web.swbotv1.exception;

public class RepositoryException extends RuntimeException { // sirve para manejar las excepciones de la capa de repositorio

    public RepositoryException(String message){
        super(message);
    }
    
}
