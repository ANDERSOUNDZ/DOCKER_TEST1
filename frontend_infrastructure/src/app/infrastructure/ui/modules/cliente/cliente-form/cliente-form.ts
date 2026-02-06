import { Component, inject, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteRepository } from '../../../../../domain/repository/cliente/cliente.repository';
import { Alert } from '../../../../../core/services/alert/alert';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cliente-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-form.html',
  styleUrl: './cliente-form.scss',
})
export class ClienteForm {
  private fb = inject(FormBuilder);
  private clienteRepo = inject(ClienteRepository);
  private notify = inject(Alert);

  close = output<void>();
  saved = output<void>();

  form = this.fb.group({
    nombre: ['', [Validators.required]],
    identificacion: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9]{10,13}$')]],
    direccion: ['', [Validators.required]],
    telefono: ['', [Validators.required]],
    genero: ['', [Validators.required]],
    edad: [null, [Validators.required, Validators.min(18)]],
    contrasena: ['', [Validators.required, Validators.minLength(4)]],
  });

  submit() {
    if (this.form.invalid) return;

    this.clienteRepo.create(this.form.value as any).subscribe({
      next: (res) => {
        this.notify.show(res.message, 'success');
        this.saved.emit();
        this.close.emit();
      },
      error: (err) => {
        const apiError = err.error;
        if (err.status === 400 && apiError.message && typeof apiError.message === 'object') {
          Object.keys(apiError.message).forEach((field) => {
            this.form.get(field)?.setErrors({ serverError: apiError.message[field] });
          });
        }
      },
    });
  }

  onlyLetters(event: KeyboardEvent) {
    const pattern = /[a-zA-ZáéíóúÁÉÍÓÚñÑ ]/;
    const inputChar = String.fromCharCode(event.charCode);
    if (!pattern.test(inputChar)) {
      event.preventDefault();
    }
  }

  getErrorMessage(controlName: string, defaultMsg: string): string {
    const control = this.form.get(controlName);
    return control?.hasError('serverError') ? control.getError('serverError') : defaultMsg;
  }
}
