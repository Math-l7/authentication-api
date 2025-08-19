package com.matheusrodrigues.authentication_api.Exceptions;

public class UserNotFoundException extends RuntimeException{
    
    public UserNotFoundException() {
        super("Usuário não identificado.");
    }
}
