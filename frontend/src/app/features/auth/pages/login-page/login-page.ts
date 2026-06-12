import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { form, required, email, maxLength, FormField, submit } from '@angular/forms/signals';
import { LoginRequest } from '../../interfaces/login-request';
import { FormFieldError } from '@app/shared/components/form-field-error/form-field-error';
import { firstValueFrom } from 'rxjs';
import { AuthState } from '../../services/auth-state';

@Component({
  selector: 'app-login-page',
  imports: [RouterModule, FormField, FormFieldError],
  templateUrl: './login-page.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginPage {
  private readonly authState = inject(AuthState);
  private readonly router = inject(Router);

  protected readonly loginModel = signal<LoginRequest>({
    email: '',
    password: '',
  });

  protected readonly loginForm = form(this.loginModel, schema => {
    required(schema.email, { message: 'Email is required' });
    email(schema.email, { message: 'Invalid email' });

    required(schema.password, { message: 'Password is required' });
    maxLength(schema.password, 20, { message: 'Password must be at most 20 characters long' });
  });

  public onSubmit(event: Event) {
    event.preventDefault();
    submit(this.loginForm, async () => {
      const credentials = this.loginModel();
      const response = await firstValueFrom(this.authState.login(credentials));
      alert(`Welcome ${response.name}`);
      this.router.navigate(['/']);
    });
  }
}
