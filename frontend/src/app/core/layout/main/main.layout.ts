import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-main',
  imports: [],
  templateUrl: './main.layout.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MainLayout {}
