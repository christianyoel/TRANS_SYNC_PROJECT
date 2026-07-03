import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Viaje, ViajeEstado } from '../../core/models/viaje.model';
import { ViajeService } from '../../core/services/viaje.service';

@Component({
  selector: 'app-admin-viajes',
  standalone: true,
  imports: [FormsModule, DatePipe, DecimalPipe],
  templateUrl: './admin-viajes.html',
  styleUrl: './admin-viajes.css',
})
export class AdminViajes implements OnInit {
  viajes: Viaje[] = [];
  cargando = true;
  guardando = false;
  mensajeError = '';
  mensajeExito = '';

  nuevoViaje = {
    numeroViaje: '',
    origen: 'Juliaca',
    destino: 'Cusco',
    fechaSalida: '',
    precio: 45,
    capacidad: 7,
  };

  private viajeService = inject(ViajeService);

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.viajeService.listarTodos().subscribe({
      next: (data) => {
        this.viajes = data;
        this.cargando = false;
      },
      error: () => {
        this.mensajeError = 'Error al cargar viajes.';
        this.cargando = false;
      },
    });
  }

  crear(): void {
    if (!this.nuevoViaje.numeroViaje || !this.nuevoViaje.fechaSalida) {
      this.mensajeError = 'Completa número de viaje y fecha.';
      return;
    }

    this.guardando = true;
    this.mensajeError = '';
    this.mensajeExito = '';

    this.viajeService
      .crear({
        ...this.nuevoViaje,
        fechaSalida: new Date(this.nuevoViaje.fechaSalida).toISOString(),
      })
      .subscribe({
        next: () => {
          this.mensajeExito = 'Viaje creado correctamente.';
          this.guardando = false;
          this.nuevoViaje.numeroViaje = '';
          this.cargar();
        },
        error: (err) => {
          this.mensajeError = err.error?.message || 'Error al crear viaje.';
          this.guardando = false;
        },
      });
  }

  cambiarEstado(numeroViaje: string, estado: ViajeEstado): void {
    this.viajeService.cambiarEstado(numeroViaje, estado).subscribe({
      next: () => this.cargar(),
      error: (err) => {
        this.mensajeError = err.error?.message || 'Error al cambiar estado.';
      },
    });
  }

  estadoClass(estado: string): string {
    switch (estado) {
      case 'ACTIVO':
        return 'bg-success';
      case 'COMPLETO':
        return 'bg-warning text-dark';
      case 'CANCELADO':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }
}
