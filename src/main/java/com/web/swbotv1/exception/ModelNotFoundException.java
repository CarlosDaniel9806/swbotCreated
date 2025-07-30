package com.web.swbotv1.exception;



public class ModelNotFoundException extends RuntimeException { // sirve para manejar las excepciones

    public  ModelNotFoundException(String message) {
        super(message);
    }
    
}
