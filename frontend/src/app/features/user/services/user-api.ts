import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment.development';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root',
})
export class UserApi {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiURL}/user`;

  getMe() {
    return this.http.get<User>(`${this.url}/me`);
  }
}
