import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';

// ── Interceptores HTTP globales ──────────────────────────────────────────────
// 1. authInterceptor   → adjunta el Bearer token JWT a cada petición saliente.
// 2. errorInterceptor  → maneja errores 401/403 globalmente: hace logout y
//                        redirige al login sin necesidad de hacerlo en cada
//                        componente o servicio.
// El orden importa: auth primero para que error pueda capturar su 401.
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(
      withInterceptors([
        authInterceptor,   // 1º: añade Authorization: Bearer <token>
        errorInterceptor,  // 2º: captura errores 401/403 y hace logout
      ]),
    ),
  ],
};
