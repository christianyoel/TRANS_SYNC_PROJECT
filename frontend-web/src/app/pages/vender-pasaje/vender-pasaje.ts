import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AsientosDisponibles, Pasaje } from '../../core/models/pasaje.model';
import { Viaje } from '../../core/models/viaje.model';
import { PasajeService } from '../../core/services/pasaje.service';
import { ViajeService } from '../../core/services/viaje.service';

@Component({
  selector: 'app-vender-pasaje',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './vender-pasaje.html',
  styleUrl: './vender-pasaje.css',
})
export class VenderPasaje implements OnInit {
  viajes: Viaje[] = [];
  viajeSeleccionado = '';
  asientosInfo: AsientosDisponibles | null = null;

  documentoPasajero = '';
  nombrePasajero = '';
  asientoSeleccionado: number | null = null;

  cargandoViajes = true;
  cargandoAsientos = false;
  vendiendo = false;
  mensajeError = '';
  mensajeExito = '';
  ultimoPasaje: Pasaje | null = null;

  private viajeService = inject(ViajeService);
  private pasajeService = inject(PasajeService);

  ngOnInit(): void {
    this.viajeService.listarActivos().subscribe({
      next: (data) => {
        this.viajes = data;
        this.cargandoViajes = false;
      },
      error: () => {
        this.mensajeError = 'Error al cargar viajes.';
        this.cargandoViajes = false;
      },
    });
  }

  onViajeChange(): void {
    this.asientosInfo = null;
    this.asientoSeleccionado = null;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.ultimoPasaje = null;

    if (!this.viajeSeleccionado) return;

    this.cargandoAsientos = true;
    this.pasajeService.asientosDisponibles(this.viajeSeleccionado).subscribe({
      next: (info) => {
        this.asientosInfo = info;
        this.cargandoAsientos = false;
      },
      error: (err) => {
        this.mensajeError = err.error?.message || 'No se pudieron cargar los asientos.';
        this.cargandoAsientos = false;
      },
    });
  }

  seleccionarAsiento(numero: number): void {
    this.asientoSeleccionado = numero;
  }

  vender(): void {
    if (!this.viajeSeleccionado || !this.documentoPasajero || !this.nombrePasajero || !this.asientoSeleccionado) {
      this.mensajeError = 'Completa todos los campos y selecciona un asiento.';
      return;
    }

    this.vendiendo = true;
    this.mensajeError = '';
    this.mensajeExito = '';

    this.pasajeService
      .vender({
        numeroViaje: this.viajeSeleccionado,
        asiento: this.asientoSeleccionado,
        documentoPasajero: this.documentoPasajero,
        nombrePasajero: this.nombrePasajero,
      })
      .subscribe({
        next: (pasaje) => {
          this.ultimoPasaje = pasaje;
          this.mensajeExito = '¡Pasaje vendido correctamente!';
          this.vendiendo = false;
          this.documentoPasajero = '';
          this.nombrePasajero = '';
          this.asientoSeleccionado = null;
          this.onViajeChange();
        },
        error: (err) => {
          this.mensajeError = err.error?.message || 'Error al vender el pasaje.';
          this.vendiendo = false;
        },
      });
  }

  get viajeActual(): Viaje | undefined {
    return this.viajes.find((v) => v.numeroViaje === this.viajeSeleccionado);
  }
}
