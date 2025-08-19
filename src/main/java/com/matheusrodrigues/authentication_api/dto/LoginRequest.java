package com.matheusrodrigues.authentication_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    
    private String email;
    private String password;
    private String username;
}
