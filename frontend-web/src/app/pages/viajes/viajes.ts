import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Viaje } from '../../core/models/viaje.model';
import { ViajeService } from '../../core/services/viaje.service';

@Component({
  selector: 'app-viajes',
  standalone: true,
  imports: [DatePipe, DecimalPipe, RouterLink],
  templateUrl: './viajes.html',
  styleUrl: './viajes.css',
})
export class Viajes implements OnInit {
  viajes: Viaje[] = [];
  cargando = true;
  mensajeError = '';

  private viajeService = inject(ViajeService);

  ngOnInit(): void {
    this.cargarViajes();
  }

  cargarViajes(): void {
    this.cargando = true;
    this.viajeService.listarActivos().subscribe({
      next: (data) => {
        this.viajes = data;
        this.cargando = false;
      },
      error: () => {
        this.mensajeError = 'No se pudieron cargar los viajes. Verifica que el backend esté activo.';
        this.cargando = false;
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
