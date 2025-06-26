package com.vital_plus.sistema_hospitalar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String senha;
}
