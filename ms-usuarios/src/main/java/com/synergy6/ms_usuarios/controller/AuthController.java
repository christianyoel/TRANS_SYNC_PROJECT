package com.synergy6.ms_usuarios.controller;

import com.synergy6.ms_usuarios.dto.LoginRequestDto;
import com.synergy6.ms_usuarios.dto.LoginResponseDto;
import com.synergy6.ms_usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }
}
