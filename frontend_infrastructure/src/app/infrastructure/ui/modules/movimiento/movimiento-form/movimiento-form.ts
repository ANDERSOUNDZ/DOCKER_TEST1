import { Component, EventEmitter, inject, Output, signal } from '@angular/core';
import { MovimientoRepository } from '../../../../../domain/repository/movimiento/movimiento.repository';
import { Alert } from '../../../../../core/services/alert/alert';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-movimiento-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './movimiento-form.html',
  styleUrl: './movimiento-form.scss',
})
export class MovimientoForm {
  private movRepo = inject(MovimientoRepository);
  private notify = inject(Alert);

  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();

  loading = signal(false);
  formData = {
    numeroCuenta: '',
    tipoMovimiento: 'Deposito',
    valor: 0,
  };

  save() {
    if (!this.formData.numeroCuenta || this.formData.valor <= 0) {
      this.notify.show('Por favor complete todos los campos correctamente', 'error');
      return;
    }

    this.loading.set(true);
    this.movRepo.create(this.formData).subscribe({
      next: () => {
        this.notify.show('Transacción realizada con éxito', 'success');
        this.loading.set(false);
        this.saved.emit();
        this.close.emit();
      },
      error: (err) => {
        this.loading.set(false);
        const msg = err.error?.message || 'Error al procesar el movimiento';
        this.notify.show(msg, 'error');
      },
    });
  }
}
