import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../models/encomienda.model';

export interface AuditLog {
  id: number;
  usuarioEmail: string;
  usuarioRol: string;
  accion: string;
  recurso: string;
  recursoId: number;
  detalle: string;
  timestamp: string;
}

@Injectable({ providedIn: 'root' })
export class AuditoriaService {
  private readonly base = `${environment.apiUrl}/api/auditoria`;

  constructor(private readonly http: HttpClient) {}

  listar(page = 0, size = 15): Observable<PageResponse<AuditLog>> {
    const params = new HttpParams()
      .set('page', page).set('size', size);
    return this.http.get<PageResponse<AuditLog>>(this.base, { params });
  }

  porRecurso(recurso: string, page = 0, size = 15): Observable<PageResponse<AuditLog>> {
    const params = new HttpParams()
      .set('page', page).set('size', size);
    return this.http.get<PageResponse<AuditLog>>(
      `${this.base}/recurso/${recurso}`, { params });
  }
}
