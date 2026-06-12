import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './features/auth/interceptors/auth-interceptor';
import { httpLoggingInterceptor } from './core/interceptors/http-logging-interceptor';
import { registerLocaleData } from '@angular/common';
import ptBR from '@angular/common/locales/pt';

registerLocaleData(ptBR, 'pt-BR');

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withFetch(), withInterceptors([httpLoggingInterceptor, authInterceptor])),
    provideBrowserGlobalErrorListeners(),
    provideClientHydration(withEventReplay()),
    provideRouter(routes),
  ],
};
