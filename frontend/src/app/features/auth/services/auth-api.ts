import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { RegisterRequest } from '../interfaces/register-request';
import { AuthResponse } from '../interfaces/auth-response';
import { LoginRequest } from '../interfaces/login-request';

@Injectable({
  providedIn: 'root',
})
export class AuthApi {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiURL}/auth`;

  register(user: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.url}/register`, user);
  }

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.url}/login`, credentials);
  }

  refresh(refreshToken: string) {
    return this.http.post<AuthResponse>(`${this.url}/refresh`, { refreshToken });
  }

  logout(refreshToken: string) {
    return this.http.post<void>(`${this.url}/logout`, { refreshToken });
  }
}
