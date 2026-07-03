import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario, UsuarioRequest } from '../models/usuario.model';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly baseUrl = `${environment.apiUrl}/api/usuarios`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.baseUrl);
  }

  registrar(request: UsuarioRequest): Observable<Usuario> {
    return this.http.post<Usuario>(this.baseUrl, request);
  }

  desactivar(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.baseUrl}/${id}/desactivar`, {});
  }
}
