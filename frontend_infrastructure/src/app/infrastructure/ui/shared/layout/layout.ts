import { Component, inject } from '@angular/core';
import { Navbar } from '../navbar/navbar';
import { Sidebar } from '../sidebar/sidebar';
import { RouterOutlet } from '@angular/router';
import { Alert } from '../../../../core/services/alert/alert';

@Component({
  selector: 'app-layout',
  imports: [Navbar, Sidebar, RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
})
export class Layout {
  public notify = inject(Alert);
}
