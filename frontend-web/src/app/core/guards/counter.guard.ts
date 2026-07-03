import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard para rutas de COUNTER.
 * Permite el acceso si el usuario está autenticado y tiene rol COUNTER o ADMIN.
 * Si no está autenticado redirige a /login; si está autenticado pero sin el
 * rol necesario redirige al home raíz.
 */
export const counterGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    return router.createUrlTree(['/login']);
  }

  if (auth.hasRole('COUNTER', 'ADMIN')) {
    return true;
  }

  return router.createUrlTree(['/']);
};
