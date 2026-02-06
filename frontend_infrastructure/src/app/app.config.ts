import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { ClienteRepository } from './domain/repository/cliente/cliente.repository';
import { ClienteService } from './core/services/cliente/cliente.service';

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: ClienteRepository, useClass: ClienteService },
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
  ],
};
