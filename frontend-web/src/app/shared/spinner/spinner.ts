import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { LoadingService } from '../../core/services/loading.service';

/**
 * Spinner global — se coloca una sola vez en app.html.
 * Se muestra automáticamente mientras haya peticiones HTTP activas.
 */
@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [AsyncPipe],
  template: `
    @if (loading.cargando$ | async) {
      <div class="spinner-overlay">
        <div class="spinner-box">
          <div class="spinner-border text-primary" style="width:3rem;height:3rem" role="status">
            <span class="visually-hidden">Cargando...</span>
          </div>
        </div>
      </div>
    }
  `,
  styles: [`
    .spinner-overlay {
      position: fixed;
      inset: 0;
      background: rgba(255, 255, 255, 0.6);
      z-index: 9999;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .spinner-box {
      background: white;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 4px 24px rgba(0,0,0,0.15);
    }
  `],
})
export class Spinner {
  readonly loading = inject(LoadingService);
}
