import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-auth',
  imports: [RouterOutlet],
  host: {
    class: 'w-screen h-screen relative flex flex-col'
  },
  templateUrl: './auth.layout.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AuthLayout {}
