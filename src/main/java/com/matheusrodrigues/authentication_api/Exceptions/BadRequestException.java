package com.matheusrodrigues.authentication_api.Exceptions;

public class BadRequestException extends RuntimeException{

        public BadRequestException() {
            super("Dado inválido.");
        }
}
