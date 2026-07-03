package com.synergy6.ms_usuarios.config;

import com.synergy6.ms_usuarios.model.Rol;
import com.synergy6.ms_usuarios.model.Usuario;
import com.synergy6.ms_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("admin@trans-sync.com")) {
            Usuario admin = new Usuario();
            admin.setNombres("Administrador");
            admin.setApellidos("TRANS-SYNC");
            admin.setDocumentoIdentidad("00000001");
            admin.setEmail("admin@trans-sync.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
        }

        usuarioRepository.findByEmail("counter@trans-sync.com").ifPresentOrElse(
                this::ensureCounterDemoCredentials,
                () -> usuarioRepository.findFirstByRol(Rol.COUNTER).ifPresentOrElse(
                        counter -> {
                            counter.setEmail("counter@trans-sync.com");
                            ensureCounterDemoCredentials(counter);
                        },
                        this::createCounterDemoUser));
    }

    private void ensureCounterDemoCredentials(Usuario counter) {
        counter.setPassword(passwordEncoder.encode("counter123"));
        counter.setRol(Rol.COUNTER);
        counter.setActivo(true);
        usuarioRepository.save(counter);
    }

    private void createCounterDemoUser() {
        Usuario counter = new Usuario();
        counter.setNombres("María Elena");
        counter.setApellidos("Condori");
        counter.setEmail("counter@trans-sync.com");
        counter.setPassword(passwordEncoder.encode("counter123"));
        counter.setRol(Rol.COUNTER);
        counter.setDocumentoIdentidad(
                usuarioRepository.existsByDocumentoIdentidad("76543210") ? "76543211" : "76543210");
        usuarioRepository.save(counter);
    }
}
