import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AsientosDisponibles, Pasaje, VenderPasajeRequest } from '../models/pasaje.model';

@Injectable({ providedIn: 'root' })
export class PasajeService {
  private readonly baseUrl = `${environment.apiUrl}/api/pasajes`;

  constructor(private readonly http: HttpClient) {}

  buscar(documento: string, numeroViaje: string): Observable<Pasaje> {
    const params = new HttpParams().set('documento', documento).set('numeroViaje', numeroViaje);
    return this.http.get<Pasaje>(`${this.baseUrl}/buscar`, { params });
  }

  vender(request: VenderPasajeRequest): Observable<Pasaje> {
    return this.http.post<Pasaje>(`${this.baseUrl}/vender`, request);
  }

  historial(documento: string): Observable<Pasaje[]> {
    const params = new HttpParams().set('documento', documento);
    return this.http.get<Pasaje[]>(`${this.baseUrl}/historial`, { params });
  }

  asientosDisponibles(numeroViaje: string): Observable<AsientosDisponibles> {
    return this.http.get<AsientosDisponibles>(`${this.baseUrl}/viajes/${numeroViaje}/asientos-disponibles`);
  }

  cancelar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
