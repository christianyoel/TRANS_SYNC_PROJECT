import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

/**
 * LoadingService
 * Centraliza el estado de carga global del sistema.
 * El LoadingInterceptor lo activa antes de cada petición HTTP y
 * lo desactiva cuando la petición termina (éxito o error).
 * El componente de spinner lo suscribe para mostrar/ocultar el overlay.
 */
@Injectable({ providedIn: 'root' })
export class LoadingService {
  private contador = 0;
  private readonly _cargando$ = new BehaviorSubject<boolean>(false);

  readonly cargando$: Observable<boolean> = this._cargando$.asObservable();

  mostrar(): void {
    this.contador++;
    this._cargando$.next(true);
  }

  ocultar(): void {
    this.contador = Math.max(0, this.contador - 1);
    if (this.contador === 0) {
      this._cargando$.next(false);
    }
  }
}
