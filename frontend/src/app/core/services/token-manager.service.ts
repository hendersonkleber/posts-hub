import { inject, Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service'

@Injectable({
  providedIn: 'root',
})
export class TokenManagerService {
  private readonly cookieService = inject(CookieService)

  obterTokens() {
    return {
      accessToken: this.cookieService.get('ph_access_token'),
      refreshToken: this.cookieService.get('ph_refresh_token')
    }
  }

  atualizarTokens(tokens: { accessToken: string; refreshToken: string }) {
    const accessTokenExpiresIn = new Date().setSeconds(300)
    const refreshTokenExpiresIn = new Date().setSeconds(86400)

    this.cookieService.set('ph_access_token', tokens.accessToken, { path: '/', expires: accessTokenExpiresIn, secure: true, sameSite: 'Strict' })
    this.cookieService.set('ph_refresh_token', tokens.refreshToken, { path: '/', expires: refreshTokenExpiresIn, secure: true, sameSite: 'Strict' })
  }

  removerTokens() {
    this.cookieService.delete('ph_access_token')
    this.cookieService.delete('ph_refresh_token')
  }
}
