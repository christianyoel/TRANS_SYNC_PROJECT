package com.synergy6.ms_usuarios.dto;

import com.synergy6.ms_usuarios.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateDto {

    @Size(max = 100)
    private String nombres;

    @Size(max = 100)
    private String apellidos;

    @Email(message = "El email no es válido")
    @Size(max = 100)
    private String email;

    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    private Rol rol;
}
