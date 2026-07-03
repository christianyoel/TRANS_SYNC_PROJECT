import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormsModule,
} from '@angular/forms';
import {
  Encomienda,
  EncomiendaRequest,
  EstadoEncomienda,
  PageResponse,
} from '../../core/models/encomienda.model';
import { EncomiendaService } from '../../core/services/encomienda.service';

/**
 * AdministrarEncomiendas
 *
 * Usa ReactiveFormsModule (FormGroup + Validators) para el formulario de
 * registro y el de edición inline. Los controles de filtro/búsqueda siguen
 * con ngModel porque son controles de UI simples que no se envían al backend.
 */
@Component({
  selector: 'app-administrar-encomiendas',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, DatePipe, DecimalPipe],
  templateUrl: './admin-encomiendas.html',
  styleUrl: './admin-encomiendas.css',
})
export class AdministrarEncomiendas implements OnInit {

  // ── Estado de la lista ────────────────────────────────────────────────────
  paginaActual: PageResponse<Encomienda> = this.paginaVacia();
  cargando = false;

  // ── Filtros y búsqueda (ngModel — controles de UI) ────────────────────────
  filtroEstado: EstadoEncomienda | '' = '';
  textoBusqueda = '';
  resultadosBusqueda: Encomienda[] | null = null;
  buscando = false;
  readonly estados: EstadoEncomienda[] = ['REGISTRADA', 'EN_TRANSITO', 'ENTREGADA', 'CANCELADA'];

  // ── Paginación ────────────────────────────────────────────────────────────
  page = 0;
  readonly size = 10;

  // ── Formulario reactivo de REGISTRO ───────────────────────────────────────
  registroForm!: FormGroup;
  guardando = false;

  // ── Formulario reactivo de EDICIÓN inline ─────────────────────────────────
  editForm!: FormGroup;
  editandoId: number | null = null;
  guardandoEdicion = false;

  // ── Feedback ──────────────────────────────────────────────────────────────
  mensajeError = '';
  mensajeExito = '';

  private readonly fb = inject(FormBuilder);
  private readonly encomiendaService = inject(EncomiendaService);

  // ── Ciclo de vida ─────────────────────────────────────────────────────────

  ngOnInit(): void {
    this.registroForm = this.crearForm('Juliaca', 'Cusco', 1, 15);
    this.editForm = this.crearForm();
    this.cargar();
  }

  // ── Helpers de formulario ─────────────────────────────────────────────────

  /** Crea un FormGroup con los validadores de negocio. */
  private crearForm(
    origen = '',
    destino = '',
    peso = 1,
    precio = 15,
  ): FormGroup {
    return this.fb.group({
      remitente:    ['', [Validators.required, Validators.minLength(3), Validators.maxLength(120)]],
      destinatario: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(120)]],
      origen:       [origen, [Validators.required, Validators.maxLength(80)]],
      destino:      [destino, [Validators.required, Validators.maxLength(80)]],
      peso:         [peso,   [Validators.required, Validators.min(0.01)]],
      precio:       [precio, [Validators.required, Validators.min(0.01)]],
    });
  }

  /** Acceso rápido a los controles del formulario de registro. */
  get r() {
    return this.registroForm.controls;
  }

  /** Acceso rápido a los controles del formulario de edición. */
  get e() {
    return this.editForm.controls;
  }

  /** Devuelve true si el control fue tocado y es inválido. */
  invalido(form: FormGroup, campo: string): boolean {
    const ctrl = form.get(campo);
    return !!(ctrl && ctrl.invalid && (ctrl.dirty || ctrl.touched));
  }

  /** Mensaje de error para un campo específico. */
  errorMsg(form: FormGroup, campo: string): string {
    const ctrl = form.get(campo);
    if (!ctrl || !ctrl.errors) return '';
    if (ctrl.errors['required'])   return 'Este campo es obligatorio.';
    if (ctrl.errors['minlength'])  return `Mínimo ${ctrl.errors['minlength'].requiredLength} caracteres.`;
    if (ctrl.errors['maxlength'])  return `Máximo ${ctrl.errors['maxlength'].requiredLength} caracteres.`;
    if (ctrl.errors['min'])        return `El valor debe ser mayor a ${ctrl.errors['min'].min}.`;
    return 'Valor inválido.';
  }

  // ── Carga y filtrado ──────────────────────────────────────────────────────

  cargar(): void {
    this.cargando = true;
    this.resultadosBusqueda = null;
    this.textoBusqueda = '';
    this.limpiarMensajes();

    const obs$ = this.filtroEstado
      ? this.encomiendaService.listarPorEstado(this.filtroEstado, this.page, this.size)
      : this.encomiendaService.listarTodas(this.page, this.size);

    obs$.subscribe({
      next: (data) => { this.paginaActual = data; this.cargando = false; },
      error: (err: Error) => { this.mostrarError(err.message); this.cargando = false; },
    });
  }

  aplicarFiltro(): void { this.page = 0; this.cargar(); }
  limpiarFiltro(): void { this.filtroEstado = ''; this.page = 0; this.cargar(); }

  // ── Búsqueda ──────────────────────────────────────────────────────────────

  buscar(): void {
    const q = this.textoBusqueda.trim();
    if (!q) { this.resultadosBusqueda = null; return; }
    this.buscando = true;
    this.limpiarMensajes();

    this.encomiendaService.buscar(q).subscribe({
      next: (data) => { this.resultadosBusqueda = data; this.buscando = false; },
      error: (err: Error) => { this.mostrarError(err.message); this.buscando = false; },
    });
  }

  limpiarBusqueda(): void {
    this.textoBusqueda = '';
    this.resultadosBusqueda = null;
  }

  // ── Paginación ────────────────────────────────────────────────────────────

  get encomiendas(): Encomienda[] {
    return this.resultadosBusqueda ?? this.paginaActual.content;
  }

  irAPagina(p: number): void {
    if (p < 0 || p >= this.paginaActual.totalPages) return;
    this.page = p;
    this.cargar();
  }

  get paginas(): number[] {
    return Array.from({ length: this.paginaActual.totalPages }, (_, i) => i);
  }

  // ── Registro (formulario reactivo) ────────────────────────────────────────

  registrar(): void {
    this.registroForm.markAllAsTouched();
    if (this.registroForm.invalid) return;

    this.guardando = true;
    this.limpiarMensajes();

    const req = this.toRequest(this.registroForm);

    this.encomiendaService.registrar(req).subscribe({
      next: () => {
        this.mostrarExito('Encomienda registrada correctamente.');
        this.registroForm.reset({ origen: 'Juliaca', destino: 'Cusco', peso: 1, precio: 15 });
        this.guardando = false;
        this.cargar();
      },
      error: (err: Error) => {
        this.mostrarError(err.message);
        this.guardando = false;
      },
    });
  }

  // ── Edición inline (formulario reactivo) ──────────────────────────────────

  iniciarEdicion(enc: Encomienda): void {
    this.editandoId = enc.id;
    this.editForm.setValue({
      remitente:    enc.remitente,
      destinatario: enc.destinatario,
      origen:       enc.origen,
      destino:      enc.destino,
      peso:         enc.peso,
      precio:       enc.precio,
    });
    this.limpiarMensajes();
  }

  cancelarEdicion(): void {
    this.editandoId = null;
    this.editForm.reset();
  }

  guardarEdicion(id: number): void {
    this.editForm.markAllAsTouched();
    if (this.editForm.invalid) return;

    this.guardandoEdicion = true;
    this.limpiarMensajes();

    this.encomiendaService.actualizar(id, this.toRequest(this.editForm)).subscribe({
      next: () => {
        this.mostrarExito('Encomienda actualizada.');
        this.editandoId = null;
        this.editForm.reset();
        this.guardandoEdicion = false;
        this.cargar();
      },
      error: (err: Error) => {
        this.mostrarError(err.message);
        this.guardandoEdicion = false;
      },
    });
  }

  // ── Cambio de estado ──────────────────────────────────────────────────────

  cambiarEstado(id: number, estado: EstadoEncomienda): void {
    this.limpiarMensajes();
    this.encomiendaService.actualizarEstado(id, estado).subscribe({
      next: () => this.cargar(),
      error: (err: Error) => this.mostrarError(err.message),
    });
  }

  // ── Eliminación ───────────────────────────────────────────────────────────

  eliminar(enc: Encomienda): void {
    if (!confirm(`¿Eliminar la encomienda #${enc.id} de ${enc.remitente}?`)) return;
    this.limpiarMensajes();
    this.encomiendaService.eliminar(enc.id).subscribe({
      next: () => { this.mostrarExito(`Encomienda #${enc.id} eliminada.`); this.cargar(); },
      error: (err: Error) => this.mostrarError(err.message),
    });
  }

  // ── Helpers de vista ──────────────────────────────────────────────────────

  estadoClass(estado: string): string {
    const m: Record<string, string> = {
      REGISTRADA: 'bg-primary', EN_TRANSITO: 'bg-warning text-dark',
      ENTREGADA: 'bg-success',  CANCELADA: 'bg-danger',
    };
    return m[estado] ?? 'bg-secondary';
  }

  estadoLabel(estado: string): string {
    const m: Record<string, string> = {
      REGISTRADA: 'Registrada', EN_TRANSITO: 'En tránsito',
      ENTREGADA: 'Entregada',   CANCELADA: 'Cancelada',
    };
    return m[estado] ?? estado;
  }

  // ── Helpers privados ──────────────────────────────────────────────────────

  private toRequest(form: FormGroup): EncomiendaRequest {
    const v = form.value;
    return {
      remitente:    v.remitente.trim(),
      destinatario: v.destinatario.trim(),
      origen:       v.origen.trim(),
      destino:      v.destino.trim(),
      peso:         +v.peso,
      precio:       +v.precio,
    };
  }

  private paginaVacia(): PageResponse<Encomienda> {
    return { content: [], page: 0, size: 10, totalElements: 0, totalPages: 0, last: true };
  }

  private limpiarMensajes(): void { this.mensajeError = ''; this.mensajeExito = ''; }

  private mostrarError(msg: string): void {
    this.mensajeError = msg;
    setTimeout(() => (this.mensajeError = ''), 6000);
  }

  private mostrarExito(msg: string): void {
    this.mensajeExito = msg;
    setTimeout(() => (this.mensajeExito = ''), 4000);
  }
}
