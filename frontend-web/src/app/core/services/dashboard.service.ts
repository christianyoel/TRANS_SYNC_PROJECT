import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ResumenEncomiendas {
  total: number;
  registradas: number;
  enTransito: number;
  entregadas: number;
  canceladas: number;
}

export interface DashboardData {
  encomiendas: ResumenEncomiendas;
  totalViajes: number;
  viajesActivos: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly api = environment.apiUrl;

  constructor(private readonly http: HttpClient) {}

  /** Carga todos los datos del dashboard en paralelo. */
  cargarDashboard(): Observable<DashboardData> {
    return new Observable(observer => {
      forkJoin({
        encomiendas: this.http.get<ResumenEncomiendas>(`${this.api}/api/encomiendas/resumen`),
        viajes: this.http.get<any[]>(`${this.api}/api/viajes`),
      }).subscribe({
        next: ({ encomiendas, viajes }) => {
          observer.next({
            encomiendas,
            totalViajes: viajes.length,
            viajesActivos: viajes.filter((v: any) => v.estado === 'ACTIVO').length,
          });
          observer.complete();
        },
        error: (err) => observer.error(err),
      });
    });
  }
}
