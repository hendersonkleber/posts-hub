import {
  computed,
  DOCUMENT,
  inject,
  Injectable,
  isDevMode,
  PLATFORM_ID,
  signal,
} from '@angular/core';
import { User } from '@app/features/user/interfaces/user';
import { CookieService } from 'ngx-cookie-service';
import { KEY_ACCESS_TOKEN, KEY_REFRESH_TOKEN } from '../constants/tokens';
import { RedirectCommand, Router } from '@angular/router';
import { AuthApi } from './auth-api';
import {
  catchError,
  EMPTY,
  finalize,
  map,
  Observable,
  of,
  shareReplay,
  switchMap,
  take,
  tap,
  throwError,
} from 'rxjs';
import { LoginRequest } from '../interfaces/login-request';
import { UserApi } from '@app/features/user/services/user-api';
import { RegisterRequest } from '../interfaces/register-request';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AuthState {
  private readonly router = inject(Router);
  private readonly authApi = inject(AuthApi);
  private readonly userApi = inject(UserApi);
  private readonly cookieService = inject(CookieService);
  private readonly platformId = inject(PLATFORM_ID);

  private readonly _profile = signal<User | null>(null);
  private readonly _accessToken = signal<string | null>(this.cookieService.get(KEY_ACCESS_TOKEN));
  private readonly _refreshToken = signal<string | null>(this.cookieService.get(KEY_REFRESH_TOKEN));
  private _hydrationProfile$: Observable<User | null> | null = null;

  public readonly user = this._profile.asReadonly();
  public readonly accessToken = this._accessToken.asReadonly();
  public readonly refreshToken = this._refreshToken.asReadonly();
  public readonly isAuthenticate = computed(() => !!this._profile());

  hydrate(): Observable<User | null> {
    if (this._profile()) return of(this._profile());

    const hasToken = !!this.accessToken() && !!this.refreshToken();
    if (!hasToken) return of(null);

    if (this._hydrationProfile$) return this._hydrationProfile$;

    this._hydrationProfile$ = this.userApi.getMe().pipe(
      tap(profile => {
        this._profile.set(profile);
        this._hydrationProfile$ = null;
      }),
      catchError(() => {
        this._hydrationProfile$ = null;
        return of(null);
      }),
      shareReplay(1),
    );

    return this._hydrationProfile$;
  }

  register(payload: RegisterRequest) {
    return this.authApi.register(payload).pipe(
      tap(response => this.persistTokens(response)),
      switchMap(() => this.userApi.getMe()),
      tap(response => this._profile.set(response)),
    );
  }

  login(credentials: LoginRequest) {
    return this.authApi.login(credentials).pipe(
      tap(response => this.persistTokens(response)),
      switchMap(() => this.userApi.getMe()),
      tap(response => this._profile.set(response)),
    );
  }

  refresh() {
    const token = this.refreshToken();

    if (!token) throw new Error('No refresh token available');

    return this.authApi.refresh(token).pipe(tap(response => this.persistTokens(response)));
  }

  logout() {
    const token = this.refreshToken();

    this.clearTokens();
    this.clearProfile();
    this.router.navigate(['/auth/login']);

    if (token) this.authApi.logout(token).pipe(take(1)).subscribe();
  }

  private persistTokens(tokens: { accessToken: string; refreshToken: string }) {
    this._accessToken.set(tokens.accessToken);
    this._refreshToken.set(tokens.refreshToken);

    const accessTokenExpiresAt = new Date(Date.now() + 300 * 1000);
    const refreshTokenExpiresAt = new Date(Date.now() + 86400 * 1000);

    this.cookieService.set(KEY_ACCESS_TOKEN, tokens.accessToken, {
      path: '/',
      expires: accessTokenExpiresAt,
      secure: !isDevMode(),
      sameSite: 'Strict',
    });

    this.cookieService.set(KEY_REFRESH_TOKEN, tokens.refreshToken, {
      path: '/',
      expires: refreshTokenExpiresAt,
      secure: !isDevMode(),
      sameSite: 'Strict',
    });
  }

  private clearProfile() {
    this._profile.set(null);
  }

  private clearTokens() {
    this._accessToken.set(null);
    this._refreshToken.set(null);

    this.cookieService.delete(KEY_ACCESS_TOKEN);
    this.cookieService.delete(KEY_REFRESH_TOKEN);
  }
}
