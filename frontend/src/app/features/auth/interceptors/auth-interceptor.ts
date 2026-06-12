import {
  HttpErrorResponse,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { AuthState } from '../services/auth-state';

let isRefreshing = false;
let accessToken$ = new BehaviorSubject<string | null>(null);

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authState = inject(AuthState);
  const token = authState.accessToken();

  return next(addAccessToken(request, token)).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) return handle401Error(next, request, authState);

      return throwError(() => error);
    }),
  );
};

function addAccessToken(request: HttpRequest<unknown>, accessToken: string | null) {
  if (!accessToken) return request;

  return request.clone({ setHeaders: { Authorization: `Bearer ${accessToken}` } });
}

function handle401Error(next: HttpHandlerFn, request: HttpRequest<unknown>, authState: AuthState) {
  if (isRefreshing) {
    return accessToken$.pipe(
      filter(token => !!token && token !== null),
      take(1),
      switchMap(token => next(addAccessToken(request, token))),
    );
  }

  isRefreshing = true;
  accessToken$.next(null);

  return authState.refresh().pipe(
    switchMap(response => {
      isRefreshing = false;
      accessToken$.next(response.accessToken);
      return next(addAccessToken(request, response.accessToken));
    }),
    catchError((error: HttpErrorResponse) => {
      isRefreshing = false;
      authState.logout();
      return throwError(() => error);
    }),
  );
}
