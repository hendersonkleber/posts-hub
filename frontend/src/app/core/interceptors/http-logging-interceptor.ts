import { formatDate } from '@angular/common';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpInterceptorFn,
  HttpResponse,
} from '@angular/common/http';
import { isDevMode } from '@angular/core';
import { catchError, map, throwError } from 'rxjs';

export const httpLoggingInterceptor: HttpInterceptorFn = (request, next) => {
  if (isDevMode()) {
    const startTime = new Date();

    return next(request).pipe(
      map((response: HttpEvent<unknown>) => {
        if (response instanceof HttpResponse) {
          const endTime = new Date();
          const duration = endTime.getTime() - startTime.getTime();

          console.log('✅ HTTP RESPONSE =>', {
            url: request.urlWithParams,
            status: response.status,
            method: request.method,
            params: request.params.toString(),
            headers: request.headers,
            duration: duration,
            startTime: formatDate(startTime, 'dd/MM/yyyy HH:mm:ss.SSS', 'pt-BR'),
            endTime: formatDate(endTime, 'dd/MM/yyyy HH:mm:ss.SSS', 'pt-BR'),
            request: request,
            response: response,
          });
        }

        return response;
      }),
      catchError((error: HttpErrorResponse) => {
        const endTime = new Date();
        const duration = endTime.getTime() - startTime.getTime();

        console.error('❌ HTTP ERROR =>', {
          url: request.urlWithParams,
          method: request.method,
          status: error.status,
          message: error.message,
          params: request.params.toString(),
          headers: request.headers,
          duration: duration,
          startTime: formatDate(startTime, 'dd/MM/yyyy HH:mm:ss.SSS', 'pt-BR'),
          endTime: formatDate(endTime, 'dd/MM/yyyy HH:mm:ss.SSS', 'pt-BR'),
          request: request,
          error: error,
        });

        return throwError(() => error);
      }),
    );
  }

  return next(request);
};
