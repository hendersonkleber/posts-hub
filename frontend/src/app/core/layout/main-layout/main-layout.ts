import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AuthState } from '@app/features/auth/services/auth-state';

@Component({
  selector: 'app-main-layout',
  imports: [],
  templateUrl: './main-layout.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MainLayout {
  private readonly authState = inject(AuthState);

  public readonly user = this.authState.user;

  logout() {
    this.authState.logout();
  }
}
