import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * adminGuard — permite acceso solo a usuarios con rol ADMIN.
 * Sin autenticación → redirige a /login.
 * Autenticado pero sin rol ADMIN → redirige a /acceso-denegado.
 */
export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    return router.createUrlTree(['/login']);
  }

  if (auth.hasRole('ADMIN')) {
    return true;
  }

  return router.createUrlTree(['/acceso-denegado']);
};
