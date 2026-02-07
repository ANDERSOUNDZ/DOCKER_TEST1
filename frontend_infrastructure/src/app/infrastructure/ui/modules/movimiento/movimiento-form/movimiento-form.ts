import { Component, EventEmitter, inject, Output, signal } from '@angular/core';
import { MovimientoRepository } from '../../../../../domain/repository/movimiento/movimiento.repository';
import { Alert } from '../../../../../core/services/alert/alert';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-movimiento-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movimiento-form.html',
  styleUrl: './movimiento-form.scss',
})
export class MovimientoForm {
  private fb = inject(FormBuilder);
  private movRepo = inject(MovimientoRepository);
  private notify = inject(Alert);

  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();

  form: FormGroup;
  loading = signal(false);

  constructor() {
    this.form = this.fb.group({
      numeroCuenta: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      tipoMovimiento: ['Deposito', [Validators.required]],
      valor: [null, [Validators.required, Validators.min(0.01)]],
    });
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.movRepo.create(this.form.value).subscribe({
      next: () => {
        this.notify.show('Transacción realizada con éxito', 'success');
        this.saved.emit();
        this.close.emit();
      },
      error: (err) => {
        this.loading.set(false);
        const errorResponse = err.error?.message;
        if (errorResponse && typeof errorResponse === 'object') {
          const firstError = Object.values(errorResponse)[0] as string;
          this.notify.show(firstError, 'error');
        } else {
          this.notify.show(errorResponse || 'Error al procesar la transacción', 'error');
        }
      },
    });
  }

  isInvalid(field: string) {
    const control = this.form.get(field);
    return control ? control.invalid && (control.touched || control.dirty) : false;
  }
}
