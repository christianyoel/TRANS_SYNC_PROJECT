import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import {
  Encomienda,
  EncomiendaRequest,
  EstadoEncomienda,
  PageResponse,
} from '../models/encomienda.model';

/**
 * EncomiendaService
 *
 * Centraliza toda la lógica de negocio y comunicación HTTP del módulo
 * de encomiendas. Los componentes solo consumen Observables.
 */
@Injectable({ providedIn: 'root' })
export class EncomiendaService {
  private readonly baseUrl = `${environment.apiUrl}/api/encomiendas`;

  constructor(private readonly http: HttpClient) {}

  // ── Consultas paginadas ────────────────────────────────────────────────────

  /** Lista paginada de todas las encomiendas, ordenadas por fecha desc. */
  listarTodas(page = 0, size = 10): Observable<PageResponse<Encomienda>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http
      .get<PageResponse<Encomienda>>(this.baseUrl, { params })
      .pipe(catchError(this.handleError));
  }

  /** Lista paginada filtrada por estado. */
  listarPorEstado(
    estado: EstadoEncomienda,
    page = 0,
    size = 10,
  ): Observable<PageResponse<Encomienda>> {
    const params = new HttpParams()
      .set('estado', estado)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http
      .get<PageResponse<Encomienda>>(`${this.baseUrl}/filtrar`, { params })
      .pipe(catchError(this.handleError));
  }

  // ── Búsqueda ───────────────────────────────────────────────────────────────

  /** Búsqueda de texto libre en remitente y destinatario. */
  buscar(q: string): Observable<Encomienda[]> {
    const params = new HttpParams().set('q', q);
    return this.http
      .get<Encomienda[]>(`${this.baseUrl}/buscar`, { params })
      .pipe(catchError(this.handleError));
  }

  /** Búsqueda pública por nombre (seguimiento sin autenticación). */
  seguimiento(nombre: string): Observable<Encomienda[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http
      .get<Encomienda[]>(`${this.baseUrl}/seguimiento`, { params })
      .pipe(catchError(this.handleError));
  }

  obtenerPorId(id: number): Observable<Encomienda> {
    return this.http
      .get<Encomienda>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  // ── Mutaciones ─────────────────────────────────────────────────────────────

  validarYRegistrar(request: EncomiendaRequest): Observable<Encomienda> {
    const validationError = this.validate(request);
    if (validationError) {
      return throwError(() => new Error(validationError));
    }
    return this.registrar({
      remitente: request.remitente.trim(),
      destinatario: request.destinatario.trim(),
      origen: request.origen.trim(),
      destino: request.destino.trim(),
      peso: request.peso,
      precio: request.precio,
    });
  }

  registrar(request: EncomiendaRequest): Observable<Encomienda> {
    return this.http
      .post<Encomienda>(this.baseUrl, request)
      .pipe(catchError(this.handleError));
  }

  actualizar(id: number, request: EncomiendaRequest): Observable<Encomienda> {
    const validationError = this.validate(request);
    if (validationError) {
      return throwError(() => new Error(validationError));
    }
    return this.http
      .put<Encomienda>(`${this.baseUrl}/${id}`, {
        ...request,
        remitente: request.remitente.trim(),
        destinatario: request.destinatario.trim(),
        origen: request.origen.trim(),
        destino: request.destino.trim(),
      })
      .pipe(catchError(this.handleError));
  }

  actualizarEstado(id: number, estado: EstadoEncomienda): Observable<Encomienda> {
    return this.http
      .patch<Encomienda>(`${this.baseUrl}/${id}/estado`, { estado })
      .pipe(catchError(this.handleError));
  }

  eliminar(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  // ── Helpers privados ───────────────────────────────────────────────────────

  private validate(request: EncomiendaRequest): string | null {
    if (!request.remitente?.trim()) return 'El remitente es obligatorio.';
    if (!request.destinatario?.trim()) return 'El destinatario es obligatorio.';
    if (!request.origen?.trim()) return 'El origen es obligatorio.';
    if (!request.destino?.trim()) return 'El destino es obligatorio.';
    if (!request.peso || request.peso <= 0) return 'El peso debe ser mayor a 0.';
    if (!request.precio || request.precio <= 0) return 'El precio debe ser mayor a 0.';
    return null;
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    const message: string =
      error.error?.message ?? error.error?.error ?? error.message ?? 'Error de conexión.';
    return throwError(() => new Error(message));
  }
}
