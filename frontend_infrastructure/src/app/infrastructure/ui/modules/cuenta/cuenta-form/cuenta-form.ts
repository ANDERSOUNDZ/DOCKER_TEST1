import { Component, EventEmitter, inject, Input, OnInit, Output, signal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CuentaRepository } from '../../../../../domain/repository/cuenta/cuenta.repository';
import { ClienteRepository } from '../../../../../domain/repository/cliente/cliente.repository';
import { Alert } from '../../../../../core/services/alert/alert';
import { Cuenta } from '../../../../../domain/models/cuenta/cuenta';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cuenta-form',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './cuenta-form.html',
  styleUrl: './cuenta-form.scss',
})
export class CuentaForm implements OnInit {
  private fb = inject(FormBuilder);
  private cuentaRepo = inject(CuentaRepository);
  private clienteRepo = inject(ClienteRepository);
  private notify = inject(Alert);

  @Input() cuentaData: Cuenta | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();

  form: FormGroup;
  loading = signal(false);
  tiposCuenta = ['Ahorros', 'Corriente', 'Ahorro Programado', 'Poliza'];
  clientesList = signal<any[]>([]); // Para el buscador de clientes

  constructor() {
    this.form = this.fb.group({
      tipoCuenta: ['', [Validators.required]],
      saldoInicial: [0, [Validators.required, Validators.min(0)]],
      clienteId: [null, [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.cargarClientes();
    if (this.cuentaData) {
      this.form.patchValue({
        tipoCuenta: this.cuentaData.tipoCuenta,
        // Cambia saldoActual por la propiedad que sí exista en tu modelo Cuenta
        saldoInicial: (this.cuentaData as any).saldoActual || (this.cuentaData as any).saldoInicial,
        // Si el ID del cliente no viene directo, intenta buscarlo así:
        clienteId: this.cuentaData.clienteId || (this.cuentaData as any).id_cliente,
      });
      // Desactivamos campos que no son editables según tu Backend (updatable = false)
      this.form.get('clienteId')?.disable();
    }
  }

  cargarClientes() {
    // Cargamos los primeros clientes para el select,
    // podrías implementar un buscador más avanzado luego
    this.clienteRepo.getAll('', 0, 50).subscribe({
      next: (res) => this.clientesList.set(res.data.content),
      error: () => this.notify.show('Error al cargar clientes', 'error'),
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    this.loading.set(true);
    const rawData = this.form.getRawValue();

    const request = this.cuentaData?.id
      ? this.cuentaRepo.update(this.cuentaData.id, rawData)
      : this.cuentaRepo.create(rawData);

    request.subscribe({
      next: () => {
        this.notify.show(
          this.cuentaData ? 'Cuenta actualizada' : 'Cuenta creada con éxito',
          'success',
        );
        this.saved.emit();
        this.close.emit();
      },
      error: (err) => {
        this.loading.set(false);
        // Captura de errores manuales (ValidationException) de tu Java
        const errorMsg = err.error?.message;
        if (typeof errorMsg === 'object') {
          const firstError = Object.values(errorMsg)[0] as string;
          this.notify.show(firstError, 'error');
        } else {
          this.notify.show(errorMsg || 'Error en el servidor', 'error');
        }
      },
    });
  }
}
