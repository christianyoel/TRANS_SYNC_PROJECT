import { DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Rol } from '../../core/models/auth.model';
import { Usuario } from '../../core/models/usuario.model';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-admin-usuarios',
  standalone: true,
  imports: [FormsModule, DatePipe],
  templateUrl: './admin-usuarios.html',
  styleUrl: './admin-usuarios.css',
})
export class AdminUsuarios implements OnInit {
  usuarios: Usuario[] = [];
  cargando = true;
  guardando = false;
  mensajeError = '';
  mensajeExito = '';

  roles: Rol[] = ['ADMIN', 'COUNTER', 'CONDUCTOR'];

  nuevoUsuario = {
    nombres: '',
    apellidos: '',
    documentoIdentidad: '',
    email: '',
    password: '',
    rol: 'COUNTER' as Rol,
  };

  private usuarioService = inject(UsuarioService);

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.usuarioService.listar().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.cargando = false;
      },
      error: () => {
        this.mensajeError = 'Error al cargar usuarios.';
        this.cargando = false;
      },
    });
  }

  registrar(): void {
    if (!this.nuevoUsuario.nombres || !this.nuevoUsuario.email || !this.nuevoUsuario.password) {
      this.mensajeError = 'Completa los campos obligatorios.';
      return;
    }

    this.guardando = true;
    this.mensajeError = '';
    this.mensajeExito = '';

    this.usuarioService.registrar(this.nuevoUsuario).subscribe({
      next: () => {
        this.mensajeExito = 'Usuario registrado correctamente.';
        this.guardando = false;
        this.nuevoUsuario = {
          nombres: '',
          apellidos: '',
          documentoIdentidad: '',
          email: '',
          password: '',
          rol: 'COUNTER',
        };
        this.cargar();
      },
      error: (err) => {
        this.mensajeError = err.error?.message || 'Error al registrar usuario.';
        this.guardando = false;
      },
    });
  }

  desactivar(id: number): void {
    if (!confirm('¿Desactivar este usuario?')) return;

    this.usuarioService.desactivar(id).subscribe({
      next: () => this.cargar(),
      error: (err) => {
        this.mensajeError = err.error?.message || 'Error al desactivar.';
      },
    });
  }

  rolBadge(rol: Rol): string {
    switch (rol) {
      case 'ADMIN':
        return 'bg-danger';
      case 'COUNTER':
        return 'bg-primary';
      case 'CONDUCTOR':
        return 'bg-info text-dark';
      default:
        return 'bg-secondary';
    }
  }
}
