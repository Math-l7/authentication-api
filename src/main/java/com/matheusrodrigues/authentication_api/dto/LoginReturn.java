package com.matheusrodrigues.authentication_api.dto;

import com.matheusrodrigues.authentication_api.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReturn {
    
    private User user;
    private String token;

    public LoginReturn(User user, String token) {
        this.user = user;
        this.token = token;
    }


}
