import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DashboardData, DashboardService } from '../../core/services/dashboard.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [DecimalPipe, RouterLink],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit {
  data: DashboardData | null = null;
  cargando = true;
  error = '';

  private readonly dashboardService = inject(DashboardService);

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.dashboardService.cargarDashboard().subscribe({
      next: (d) => { this.data = d; this.cargando = false; },
      error: () => { this.error = 'Error al cargar el dashboard.'; this.cargando = false; },
    });
  }
}
