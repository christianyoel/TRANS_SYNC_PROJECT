import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';

// ── Interceptores HTTP globales (orden importante) ───────────────────────────
// 1. authInterceptor    → adjunta el Bearer token JWT a cada petición saliente.
// 2. errorInterceptor   → captura errores 401/403 y hace logout automático.
// 3. loadingInterceptor → activa/desactiva el spinner global por petición.
import { authInterceptor }    from './core/interceptors/auth.interceptor';
import { errorInterceptor }   from './core/interceptors/error.interceptor';
import { loadingInterceptor } from './core/interceptors/loading.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(
      withInterceptors([
        authInterceptor,     // 1º: añade Authorization: Bearer <token>
        errorInterceptor,    // 2º: captura errores 401/403
        loadingInterceptor,  // 3º: spinner global
      ]),
    ),
  ],
};
