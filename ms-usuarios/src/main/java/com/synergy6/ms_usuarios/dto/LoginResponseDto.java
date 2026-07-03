package com.synergy6.ms_usuarios.dto;

import com.synergy6.ms_usuarios.model.Rol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

    private String token;
    private Long userId;
    private String nombres;
    private String apellidos;
    private String email;
    private Rol rol;
}
