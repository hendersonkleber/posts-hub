import { Routes } from '@angular/router';
import { isAuthenticateGuard } from './features/auth/guards/is-authenticate-guard';

export const routes: Routes = [
  {
    path: 'auth',
    loadComponent: () => import('@app/core/layout/auth-layout/auth-layout').then(c => c.AuthLayout),
    children: [{ path: '', loadChildren: () => import('@app/features/auth/auth.routes') }],
  },
  {
    path: '',
    canActivate: [isAuthenticateGuard],
    loadComponent: () => import('@app/core/layout/main-layout/main-layout').then(c => c.MainLayout),
    children: [
      {
        path: '',
        loadComponent: () =>
          import('@app/features/home/pages/home-page/home-page').then(c => c.HomePage),
      },
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
