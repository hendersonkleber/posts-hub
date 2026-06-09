import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { RegisterRequest } from '../models/register-request';
import { AuthResponse } from '../models/auth-response';
import { LoginRequest } from '../models/login-request';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient)
  private readonly url = `${environment.apiURL}`

  register(user: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.url}/auth/register`, user)
  }

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.url}/auth/login`, credentials)
  }

  refresh(refreshToken: string) {
    return this.http.post<AuthResponse>(`${this.url}/auth/refresh`, { refreshToken })
  }
}
