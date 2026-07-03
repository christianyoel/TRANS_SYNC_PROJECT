import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Encomienda } from '../../core/models/encomienda.model';
import { EncomiendaService } from '../../core/services/encomienda.service';

/**
 * SeguimientoEncomienda
 *
 * Página pública (sin autenticación) que permite a cualquier persona
 * consultar el estado de sus envíos ingresando su nombre como remitente
 * o destinatario. Llama a GET /api/encomiendas/seguimiento?nombre=X,
 * que el API Gateway permite sin token JWT.
 */
@Component({
  selector: 'app-seguimiento-encomienda',
  standalone: true,
  imports: [FormsModule, DatePipe, DecimalPipe],
  templateUrl: './seguimiento-encomienda.html',
  styleUrl: './seguimiento-encomienda.css',
})
export class SeguimientoEncomienda {
  nombre = '';
  resultados: Encomienda[] | null = null;
  buscando = false;
  mensajeError = '';
  buscado = false;

  private readonly encomiendaService = inject(EncomiendaService);

  buscar(): void {
    const q = this.nombre.trim();
    if (!q) {
      this.mensajeError = 'Ingresa tu nombre para buscar tus envíos.';
      return;
    }

    this.buscando = true;
    this.mensajeError = '';
    this.buscado = false;

    this.encomiendaService.seguimiento(q).subscribe({
      next: (data) => {
        this.resultados = data;
        this.buscando = false;
        this.buscado = true;
      },
      error: (err: Error) => {
        this.mensajeError = err.message;
        this.buscando = false;
        this.buscado = false;
      },
    });
  }

  limpiar(): void {
    this.nombre = '';
    this.resultados = null;
    this.mensajeError = '';
    this.buscado = false;
  }

  estadoClass(estado: string): string {
    const clases: Record<string, string> = {
      REGISTRADA:  'bg-primary',
      EN_TRANSITO: 'bg-warning text-dark',
      ENTREGADA:   'bg-success',
      CANCELADA:   'bg-danger',
    };
    return clases[estado] ?? 'bg-secondary';
  }

  estadoLabel(estado: string): string {
    const labels: Record<string, string> = {
      REGISTRADA:  'Registrada',
      EN_TRANSITO: 'En tránsito',
      ENTREGADA:   'Entregada',
      CANCELADA:   'Cancelada',
    };
    return labels[estado] ?? estado;
  }

  estadoIcono(estado: string): string {
    const iconos: Record<string, string> = {
      REGISTRADA:  '📋',
      EN_TRANSITO: '🚚',
      ENTREGADA:   '✅',
      CANCELADA:   '❌',
    };
    return iconos[estado] ?? '📦';
  }

  progreso(estado: string): string {
    const pct: Record<string, string> = {
      REGISTRADA:  '15%',
      EN_TRANSITO: '55%',
      ENTREGADA:   '100%',
      CANCELADA:   '100%',
    };
    return pct[estado] ?? '0%';
  }
}
