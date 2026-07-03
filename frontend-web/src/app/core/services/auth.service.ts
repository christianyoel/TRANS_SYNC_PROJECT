import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, Rol, StoredUser } from '../models/auth.model';

const TOKEN_KEY = 'trans_sync_token';
const USER_KEY = 'trans_sync_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly currentUser = signal<StoredUser | null>(this.loadUser());

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, credentials).pipe(
      tap((response) => {
        localStorage.setItem(TOKEN_KEY, response.token);
        const user: StoredUser = {
          userId: response.userId,
          nombres: response.nombres,
          apellidos: response.apellidos,
          email: response.email,
          rol: response.rol,
        };
        localStorage.setItem(USER_KEY, JSON.stringify(user));
        this.currentUser.set(user);
      }),
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  hasRole(...roles: Rol[]): boolean {
    const user = this.currentUser();
    return !!user && roles.includes(user.rol);
  }

  getHomeRoute(): string {
    const user = this.currentUser();
    if (!user) return '/';
    if (user.rol === 'ADMIN') return '/admin/viajes';
    if (user.rol === 'COUNTER') return '/counter/vender';
    return '/';
  }

  private loadUser(): StoredUser | null {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as StoredUser;
    } catch {
      return null;
    }
  }
}
