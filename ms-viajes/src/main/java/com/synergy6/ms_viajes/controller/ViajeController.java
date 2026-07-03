package com.synergy6.ms_viajes.controller;

import com.synergy6.ms_viajes.dto.ViajeEstadoDto;
import com.synergy6.ms_viajes.dto.ViajeRequestDto;
import com.synergy6.ms_viajes.dto.ViajeResponseDto;
import com.synergy6.ms_viajes.service.ViajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final ViajeService viajeService;

    @GetMapping
    public ResponseEntity<List<ViajeResponseDto>> listar(
            @RequestParam(defaultValue = "false") boolean todos) {
        if (todos) {
            return ResponseEntity.ok(viajeService.listarTodos());
        }
        return ResponseEntity.ok(viajeService.listarActivos());
    }

    @GetMapping("/{numeroViaje}")
    public ResponseEntity<ViajeResponseDto> obtener(@PathVariable String numeroViaje) {
        return ResponseEntity.ok(viajeService.obtenerPorNumeroViaje(numeroViaje));
    }

    @PostMapping
    public ResponseEntity<ViajeResponseDto> crear(@Valid @RequestBody ViajeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(viajeService.crear(request));
    }

    @PatchMapping("/{numeroViaje}/estado")
    public ResponseEntity<ViajeResponseDto> cambiarEstado(
            @PathVariable String numeroViaje,
            @RequestBody ViajeEstadoDto request) {
        return ResponseEntity.ok(viajeService.cambiarEstado(numeroViaje, request.getEstado()));
    }

    @PatchMapping("/{numeroViaje}/completo")
    public ResponseEntity<Void> marcarCompleto(@PathVariable String numeroViaje) {
        viajeService.marcarCompleto(numeroViaje);
        return ResponseEntity.noContent().build();
    }
}
