import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { LoadingService } from '../services/loading.service';

/**
 * loadingInterceptor
 * Interceptor funcional que activa el spinner global antes de cada
 * petición HTTP y lo desactiva cuando la petición termina.
 * Se usa finalize() para garantizar que el spinner se oculta incluso
 * si la petición termina con error.
 */
export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);
  loadingService.mostrar();
  return next(req).pipe(
    finalize(() => loadingService.ocultar()),
  );
};
