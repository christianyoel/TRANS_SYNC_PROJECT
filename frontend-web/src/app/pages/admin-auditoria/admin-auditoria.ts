import { DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuditLog, AuditoriaService } from '../../core/services/auditoria.service';
import { PageResponse } from '../../core/models/encomienda.model';

@Component({
  selector: 'app-admin-auditoria',
  standalone: true,
  imports: [FormsModule, DatePipe],
  templateUrl: './admin-auditoria.html',
  styleUrl: './admin-auditoria.css',
})
export class AdminAuditoria implements OnInit {
  pagina: PageResponse<AuditLog> = this.paginaVacia();
  cargando = false;
  error = '';

  filtroRecurso = '';
  page = 0;
  readonly size = 15;
  readonly recursos = ['', 'ENCOMIENDA', 'VIAJE', 'PASAJE', 'USUARIO'];

  private readonly svc = inject(AuditoriaService);

  ngOnInit(): void { this.cargar(); }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    const obs$ = this.filtroRecurso
      ? this.svc.porRecurso(this.filtroRecurso, this.page, this.size)
      : this.svc.listar(this.page, this.size);

    obs$.subscribe({
      next: (d) => { this.pagina = d; this.cargando = false; },
      error: () => { this.error = 'Error al cargar la auditoría.'; this.cargando = false; },
    });
  }

  aplicarFiltro(): void { this.page = 0; this.cargar(); }
  limpiarFiltro(): void { this.filtroRecurso = ''; this.page = 0; this.cargar(); }

  irAPagina(p: number): void {
    if (p < 0 || p >= this.pagina.totalPages) return;
    this.page = p; this.cargar();
  }

  get paginas(): number[] {
    return Array.from({ length: this.pagina.totalPages }, (_, i) => i);
  }

  accionClass(accion: string): string {
    const m: Record<string, string> = {
      CREATE: 'bg-success', UPDATE: 'bg-primary',
      DELETE: 'bg-danger',  ESTADO_CAMBIO: 'bg-warning text-dark',
      LOGIN: 'bg-info text-dark',
    };
    return m[accion] ?? 'bg-secondary';
  }

  private paginaVacia(): PageResponse<AuditLog> {
    return { content: [], page: 0, size: 15, totalElements: 0, totalPages: 0, last: true };
  }
}
