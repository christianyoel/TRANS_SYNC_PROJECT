package com.synergy6.ms_usuarios.repository;

import com.synergy6.ms_usuarios.model.Rol;
import com.synergy6.ms_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDocumentoIdentidad(String documentoIdentidad);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findFirstByRol(Rol rol);

    boolean existsByDocumentoIdentidad(String documentoIdentidad);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
