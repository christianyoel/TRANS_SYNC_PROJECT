import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Pasaje } from '../core/models/pasaje.model';
import { PasajeService } from '../core/services/pasaje.service';

@Component({
  selector: 'app-buscar-reserva',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './buscar-reserva.html',
  styleUrl: './buscar-reserva.css',
})
export class BuscarReserva implements OnInit {
  documento = '';
  numeroViaje = '';
  cargando = false;
  mensajeError = '';
  reservaEncontrada: Pasaje | null = null;

  private pasajeService = inject(PasajeService);
  private route = inject(ActivatedRoute);

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params['viaje']) {
        this.numeroViaje = params['viaje'];
      }
    });
  }

  consultarReserva(): void {
    if (!this.documento || !this.numeroViaje) {
      this.mensajeError = 'Por favor, completa ambos campos.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.reservaEncontrada = null;

    this.pasajeService.buscar(this.documento, this.numeroViaje).subscribe({
      next: (datos) => {
        this.reservaEncontrada = datos;
        this.cargando = false;
      },
      error: (err) => {
        this.mensajeError = err.error?.message || 'No se encontró ninguna reserva.';
        this.cargando = false;
      },
    });
  }

  limpiarFormulario(): void {
    this.documento = '';
    this.numeroViaje = '';
    this.reservaEncontrada = null;
    this.mensajeError = '';
  }
}
