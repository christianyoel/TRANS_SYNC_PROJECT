import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';
  cargando = false;
  mensajeError = '';

  private auth = inject(AuthService);
  private router = inject(Router);

  iniciarSesion(): void {
    if (!this.email || !this.password) {
      this.mensajeError = 'Completa email y contraseña.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigateByUrl(this.auth.getHomeRoute());
      },
      error: (err) => {
        this.cargando = false;
        this.mensajeError = err.error?.message || 'Credenciales inválidas.';
      },
    });
  }

  usarDemo(tipo: 'admin' | 'counter'): void {
    if (tipo === 'admin') {
      this.email = 'admin@trans-sync.com';
      this.password = 'admin123';
    } else {
      this.email = 'counter@trans-sync.com';
      this.password = 'counter123';
    }
  }
}
