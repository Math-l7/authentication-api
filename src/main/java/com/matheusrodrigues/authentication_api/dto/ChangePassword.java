package com.matheusrodrigues.authentication_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChangePassword {
    
    private String olderPassword;
    private String newPassword;
}
