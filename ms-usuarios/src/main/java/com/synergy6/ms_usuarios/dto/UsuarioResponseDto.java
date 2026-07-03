package com.synergy6.ms_usuarios.dto;

import com.synergy6.ms_usuarios.model.Rol;
import com.synergy6.ms_usuarios.model.Usuario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponseDto {

    private Long id;
    private String nombres;
    private String apellidos;
    private String documentoIdentidad;
    private String email;
    private Rol rol;
    private LocalDateTime fechaCreacion;
    private Boolean activo;

    public static UsuarioResponseDto fromEntity(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .documentoIdentidad(usuario.getDocumentoIdentidad())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .fechaCreacion(usuario.getFechaCreacion())
                .activo(usuario.getActivo())
                .build();
    }
}
