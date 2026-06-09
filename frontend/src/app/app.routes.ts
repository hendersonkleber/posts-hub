import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'auth',
    loadComponent: () => import('@app/core/layout/auth/auth.layout').then(c => c.AuthLayout),
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', loadComponent: () => import('@app/features/auth/pages/login/login.page').then(c => c.LoginPage) },
      { path: 'register', loadComponent: () => import('@app/features/auth/pages/register/register.page').then(c => c.RegisterPage) }
    ]
  },
  {
    path: '',
    loadComponent: () => import('@app/core/layout/main/main.layout').then(c => c.MainLayout),
    // canActivate: () => inject(AuthState).authenticate,
    children: [
      // { path: '', loadComponent: () => import('@/features/post').then(c => c.) },
    ]
  }
];
