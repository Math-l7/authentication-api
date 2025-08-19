package com.matheusrodrigues.authentication_api.Exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException() {
        super("Não autorizado.");
    }
    
}
