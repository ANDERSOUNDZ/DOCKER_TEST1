import { Component, inject, input, OnInit, output, signal } from '@angular/core';
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
export class ClienteForm implements OnInit {
  private fb = inject(FormBuilder);
  private clienteRepo = inject(ClienteRepository);
  private notify = inject(Alert);
  clienteData = input<any | null>(null);
  cambiarPass = signal(false);

  close = output<void>();
  saved = output<void>();

  form = this.fb.group({
    nombre: ['', [Validators.required]],
    identificacion: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9]{10,13}$')]],
    direccion: ['', [Validators.required]],
    telefono: ['', [Validators.required]],
    genero: ['', [Validators.required]],
    edad: [null, [Validators.required, Validators.min(18)]],
    contrasena: [{ value: '', disabled: true }, [Validators.minLength(4)]],
  });

  ngOnInit() {
    const cliente = this.clienteData();
    if (cliente) {
      this.form.patchValue(cliente);
      this.form.get('nombre')?.disable();
      this.form.get('identificacion')?.disable();
      this.form.get('contrasena')?.disable();

      // Activar validación de estado solo para edición
      this.form.get('estado')?.setValidators([Validators.required]);
      this.form.get('estado')?.updateValueAndValidity();

      this.cambiarPass.set(false);
    } else {
      this.form.get('contrasena')?.enable();
      this.form.get('contrasena')?.setValidators([Validators.required, Validators.minLength(4)]);
    }
  }

  submit() {
    if (this.form.invalid) return;

    // Forzamos el tipo a 'any' para poder usar 'delete' sin errores de TS
    const data: any = this.form.getRawValue();

    // Si estamos editando y NO se activó el cambio de pass, la eliminamos del envío
    if (this.clienteData() && !this.cambiarPass()) {
      delete data.contrasena;
    }

    const clienteId = this.clienteData()?.id;

    const request$ = clienteId
      ? this.clienteRepo.update(clienteId, data)
      : this.clienteRepo.create(data);

    request$.subscribe({
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

  togglePasswordEdit() {
    this.cambiarPass.update((val) => !val);
    const ctrl = this.form.get('contrasena');
    if (this.cambiarPass()) {
      ctrl?.enable();
      ctrl?.setValidators([Validators.required, Validators.minLength(4)]);
    } else {
      ctrl?.disable();
      ctrl?.setValue(''); // Limpiamos si se arrepiente
      ctrl?.clearValidators();
    }
    ctrl?.updateValueAndValidity();
  }
}
