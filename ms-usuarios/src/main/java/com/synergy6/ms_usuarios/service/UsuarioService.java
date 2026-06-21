package com.synergy6.ms_usuarios.service;

import com.synergy6.ms_usuarios.model.Usuario;
import com.synergy6.ms_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    // Inyectamos el repositorio para poder hablar con PostgreSQL
    private final UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario nuevoUsuario) {
        // Regla de negocio: Validar que el DNI no esté duplicado
        if (usuarioRepository.existsByDocumentoIdentidad(nuevoUsuario.getDocumentoIdentidad())) {
            throw new RuntimeException("Error: Ya existe un usuario con el DNI " + nuevoUsuario.getDocumentoIdentidad());
        }

        // Aquí más adelante encriptaremos la contraseña, por ahora la guardamos directo
        return usuarioRepository.save(nuevoUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}