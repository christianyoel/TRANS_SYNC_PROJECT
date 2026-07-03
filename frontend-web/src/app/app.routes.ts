import { Routes } from '@angular/router';

// ── Guards ───────────────────────────────────────────────────────────────────
// authGuard    → verifica que el usuario esté autenticado (token presente)
// adminGuard   → verifica autenticación + rol ADMIN
// counterGuard → verifica autenticación + rol COUNTER o ADMIN
// roleGuard    → factory genérica para cualquier combinación de roles
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { counterGuard } from './core/guards/counter.guard';
import { roleGuard } from './core/guards/role.guard';

// ── Componentes ──────────────────────────────────────────────────────────────
import { BuscarReserva } from './buscar-reserva/buscar-reserva';
import { AdministrarEncomiendas } from './pages/admin-encomiendas/admin-encomiendas';
import { AdminUsuarios } from './pages/admin-usuarios/admin-usuarios';
import { AdminViajes } from './pages/admin-viajes/admin-viajes';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { VenderPasaje } from './pages/vender-pasaje/vender-pasaje';
import { Viajes } from './pages/viajes/viajes';

import { SeguimientoEncomienda } from './pages/seguimiento-encomienda/seguimiento-encomienda';

export const routes: Routes = [
  // ── Rutas públicas ─────────────────────────────────────────────────────────
  { path: '', component: Home },
  { path: 'buscar', component: BuscarReserva },
  { path: 'viajes', component: Viajes },
  { path: 'login', component: Login },
  { path: 'seguimiento-encomienda', component: SeguimientoEncomienda },

  // ── Rutas de COUNTER (COUNTER o ADMIN) ─────────────────────────────────────
  {
    path: 'counter/vender',
    component: VenderPasaje,
    canActivate: [counterGuard],   // usa counterGuard dedicado
  },
  {
    path: 'counter/encomiendas',
    component: AdministrarEncomiendas,
    canActivate: [counterGuard],   // COUNTER y ADMIN pueden registrar encomiendas
  },

  // ── Rutas de ADMIN ─────────────────────────────────────────────────────────
  {
    path: 'admin/viajes',
    component: AdminViajes,
    canActivate: [adminGuard],
  },
  {
    path: 'admin/usuarios',
    component: AdminUsuarios,
    canActivate: [adminGuard],
  },
  {
    path: 'admin/encomiendas',
    component: AdministrarEncomiendas,
    canActivate: [adminGuard],
  },

  // ── Fallback ───────────────────────────────────────────────────────────────
  { path: '**', redirectTo: '' },
];
