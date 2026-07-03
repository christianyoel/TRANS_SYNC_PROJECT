package com.synergy6.ms_usuarios.service;

import com.synergy6.ms_usuarios.dto.*;
import com.synergy6.ms_usuarios.exception.BusinessException;
import com.synergy6.ms_usuarios.model.Usuario;
import com.synergy6.ms_usuarios.repository.UsuarioRepository;
import com.synergy6.ms_usuarios.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas", HttpStatus.UNAUTHORIZED));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new BusinessException("Usuario inactivo", HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol());

        return LoginResponseDto.builder()
                .token(token)
                .userId(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    public UsuarioResponseDto registrarUsuario(UsuarioRequestDto dto) {
        if (usuarioRepository.existsByDocumentoIdentidad(dto.getDocumentoIdentidad())) {
            throw new BusinessException("Ya existe un usuario con el DNI " + dto.getDocumentoIdentidad());
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Ya existe un usuario con el email " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setDocumentoIdentidad(dto.getDocumentoIdentidad());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());

        return UsuarioResponseDto.fromEntity(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponseDto::fromEntity)
                .toList();
    }

    public UsuarioResponseDto obtenerPorId(Long id) {
        return UsuarioResponseDto.fromEntity(buscarEntidad(id));
    }

    public UsuarioResponseDto actualizar(Long id, UsuarioUpdateDto dto) {
        Usuario usuario = buscarEntidad(id);

        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())
                && usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("Ya existe un usuario con el email " + dto.getEmail());
        }

        if (dto.getNombres() != null) {
            usuario.setNombres(dto.getNombres());
        }
        if (dto.getApellidos() != null) {
            usuario.setApellidos(dto.getApellidos());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        }

        return UsuarioResponseDto.fromEntity(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDto desactivar(Long id) {
        Usuario usuario = buscarEntidad(id);
        usuario.setActivo(false);
        return UsuarioResponseDto.fromEntity(usuarioRepository.save(usuario));
    }

    private Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado", HttpStatus.NOT_FOUND));
    }
}
