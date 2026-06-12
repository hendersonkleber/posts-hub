import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthState } from '../services/auth-state';
import { map, of } from 'rxjs';

export const isAuthenticateGuard: CanActivateFn = (route, state) => {
  console.log('GUARD');

  const authState = inject(AuthState);
  const router = inject(Router);

  return authState
    .hydrate()
    .pipe(map(profile => !!profile || router.createUrlTree(['/auth/login'])));
};
