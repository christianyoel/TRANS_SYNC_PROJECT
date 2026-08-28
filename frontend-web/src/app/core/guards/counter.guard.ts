import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * counterGuard — permite acceso a usuarios con rol COUNTER o ADMIN.
 * Sin autenticación → redirige a /login.
 * Autenticado pero sin rol → redirige a /acceso-denegado.
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

  return router.createUrlTree(['/acceso-denegado']);
};
