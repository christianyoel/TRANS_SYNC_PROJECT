import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Viaje, ViajeEstado, ViajeRequest } from '../models/viaje.model';

@Injectable({ providedIn: 'root' })
export class ViajeService {
  private readonly baseUrl = `${environment.apiUrl}/api/viajes`;

  constructor(private readonly http: HttpClient) {}

  listarActivos(): Observable<Viaje[]> {
    return this.http.get<Viaje[]>(this.baseUrl);
  }

  listarTodos(): Observable<Viaje[]> {
    const params = new HttpParams().set('todos', 'true');
    return this.http.get<Viaje[]>(this.baseUrl, { params });
  }

  obtener(numeroViaje: string): Observable<Viaje> {
    return this.http.get<Viaje>(`${this.baseUrl}/${numeroViaje}`);
  }

  crear(request: ViajeRequest): Observable<Viaje> {
    return this.http.post<Viaje>(this.baseUrl, request);
  }

  cambiarEstado(numeroViaje: string, estado: ViajeEstado): Observable<Viaje> {
    return this.http.patch<Viaje>(`${this.baseUrl}/${numeroViaje}/estado`, { estado });
  }
}
