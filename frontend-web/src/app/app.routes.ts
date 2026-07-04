import { Routes } from '@angular/router';

// ── Guards ───────────────────────────────────────────────────────────────────
import { authGuard }    from './core/guards/auth.guard';
import { adminGuard }   from './core/guards/admin.guard';
import { counterGuard } from './core/guards/counter.guard';

// ── Componentes públicos ─────────────────────────────────────────────────────
import { Home }                  from './pages/home/home';
import { Login }                 from './pages/login/login';
import { BuscarReserva }         from './buscar-reserva/buscar-reserva';
import { Viajes }                from './pages/viajes/viajes';
import { SeguimientoEncomienda } from './pages/seguimiento-encomienda/seguimiento-encomienda';
import { NotFound }              from './pages/not-found/not-found';
import { AccesoDenegado }        from './pages/acceso-denegado/acceso-denegado';

// ── Componentes autenticados ─────────────────────────────────────────────────
import { Perfil }                from './pages/perfil/perfil';
import { VenderPasaje }          from './pages/vender-pasaje/vender-pasaje';
import { AdministrarEncomiendas }from './pages/admin-encomiendas/admin-encomiendas';
import { AdminDashboard }        from './pages/admin-dashboard/admin-dashboard';
import { AdminViajes }           from './pages/admin-viajes/admin-viajes';
import { AdminUsuarios }         from './pages/admin-usuarios/admin-usuarios';
import { AdminAuditoria }        from './pages/admin-auditoria/admin-auditoria';

export const routes: Routes = [

  // ── Rutas públicas ─────────────────────────────────────────────────────────
  { path: '',                      component: Home },
  { path: 'login',                 component: Login },
  { path: 'buscar',                component: BuscarReserva },
  { path: 'viajes',                component: Viajes },
  { path: 'seguimiento-encomienda',component: SeguimientoEncomienda },
  { path: 'acceso-denegado',       component: AccesoDenegado },

  // ── Rutas autenticadas (cualquier rol) ─────────────────────────────────────
  {
    path: 'perfil',
    component: Perfil,
    canActivate: [authGuard],
  },

  // ── Rutas de COUNTER (COUNTER o ADMIN) ─────────────────────────────────────
  {
    path: 'counter/vender',
    component: VenderPasaje,
    canActivate: [counterGuard],
  },
  {
    path: 'counter/encomiendas',
    component: AdministrarEncomiendas,
    canActivate: [counterGuard],
  },

  // ── Rutas de ADMIN ─────────────────────────────────────────────────────────
  {
    path: 'admin/dashboard',
    component: AdminDashboard,
    canActivate: [adminGuard],
  },
  {
    path: 'admin/encomiendas',
    component: AdministrarEncomiendas,
    canActivate: [adminGuard],
  },
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
    path: 'admin/auditoria',
    component: AdminAuditoria,
    canActivate: [adminGuard],
  },

  // ── 404 — debe ir siempre al final ──────────────────────────────────────────
  { path: '**', component: NotFound },
];
