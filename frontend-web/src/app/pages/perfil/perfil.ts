import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './perfil.html',
})
export class Perfil implements OnInit {
  email = '';
  rol = '';
  nombres = '';

  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    const user = this.auth.currentUser();
    if (!user) { this.router.navigate(['/login']); return; }
    this.email   = user.email;
    this.rol     = user.rol;
    this.nombres = `${user.nombres} ${user.apellidos}`;
  }

  cerrarSesion(): void {
    this.auth.logout();
  }

  rolClass(): string {
    const m: Record<string, string> = {
      ADMIN: 'bg-danger', COUNTER: 'bg-primary', CONDUCTOR: 'bg-success',
    };
    return m[this.rol] ?? 'bg-secondary';
  }

  rolDescripcion(): string {
    const m: Record<string, string> = {
      ADMIN:     'Acceso completo al sistema',
      COUNTER:   'Venta de pasajes y registro de encomiendas',
      CONDUCTOR: 'Consulta de viajes asignados',
    };
    return m[this.rol] ?? '';
  }
}
