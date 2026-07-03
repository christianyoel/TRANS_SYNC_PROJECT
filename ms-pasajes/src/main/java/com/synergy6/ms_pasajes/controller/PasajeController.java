package com.synergy6.ms_pasajes.controller;

import com.synergy6.ms_pasajes.dto.AsientosDisponiblesDto;
import com.synergy6.ms_pasajes.dto.PasajeResponseDto;
import com.synergy6.ms_pasajes.dto.VenderPasajeRequestDto;
import com.synergy6.ms_pasajes.service.PasajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajes")
@RequiredArgsConstructor
public class PasajeController {

    private final PasajeService pasajeService;

    @PostMapping("/vender")
    public ResponseEntity<PasajeResponseDto> vender(@Valid @RequestBody VenderPasajeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pasajeService.venderPasaje(request));
    }

    @GetMapping("/buscar")
    public ResponseEntity<PasajeResponseDto> buscar(
            @RequestParam String documento,
            @RequestParam String numeroViaje) {
        return ResponseEntity.ok(pasajeService.buscarReserva(documento, numeroViaje));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<PasajeResponseDto>> historial(@RequestParam String documento) {
        return ResponseEntity.ok(pasajeService.historialPorDocumento(documento));
    }

    @GetMapping("/viajes/{numeroViaje}/asientos-disponibles")
    public ResponseEntity<AsientosDisponiblesDto> asientosDisponibles(@PathVariable String numeroViaje) {
        return ResponseEntity.ok(pasajeService.asientosDisponibles(numeroViaje));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pasajeService.cancelarPasaje(id);
        return ResponseEntity.noContent().build();
    }
}
