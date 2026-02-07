import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { ClienteRepository } from './domain/repository/cliente/cliente.repository';
import { ClienteService } from './core/services/cliente/cliente.service';
import { CuentaRepository } from './domain/repository/cuenta/cuenta.repository';
import { CuentaService } from './core/services/cuenta/cuenta.service';
import { MovimientoRepository } from './domain/repository/movimiento/movimiento.repository';
import { MovimientoService } from './core/services/movimiento/movimiento.service';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { errorInterceptor } from './core/interceptors/error/error';

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: ClienteRepository, useClass: ClienteService },
    { provide: CuentaRepository, useClass: CuentaService },
    { provide: MovimientoRepository, useClass: MovimientoService },
    provideHttpClient(withInterceptors([errorInterceptor])),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
  ],
};
