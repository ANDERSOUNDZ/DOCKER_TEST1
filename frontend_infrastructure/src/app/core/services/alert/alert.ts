import { Injectable, signal } from '@angular/core';

export interface Notification {
  message: string;
  type: 'success' | 'error';
}

@Injectable({
  providedIn: 'root',
})
export class Alert {
alert = signal<Notification | null>(null);

  show(message: string, type: 'success' | 'error' = 'success') {
    this.alert.set({ message, type });
    setTimeout(() => this.alert.set(null), 4000);
  }
}
