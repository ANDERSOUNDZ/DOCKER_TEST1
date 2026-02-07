import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { MovimientoRepository } from '../../../../../domain/repository/movimiento/movimiento.repository';
import { Alert } from '../../../../../core/services/alert/alert';
import { ReporteEstadoCuenta } from '../../../../../domain/models/movimiento/reporteEstadoCuenta';

@Component({
  selector: 'app-reporte-create',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reporte-create.html',
  styleUrl: './reporte-create.scss',
})
export class ReporteCreate {
  private fb = inject(FormBuilder);
  private movRepo = inject(MovimientoRepository);
  private notify = inject(Alert);

  form!: FormGroup;
  loading = signal(false);
  reporte = signal<ReporteEstadoCuenta | null>(null);

  // Variable para limitar el calendario en el HTML
  today = new Date().toISOString().split('T')[0];

  ngOnInit(): void {
    this.form = this.fb.group({
      clienteId: [null, [Validators.required, Validators.min(1)]],
      // Añadimos el validador personalizado
      inicio: ['', [Validators.required, this.fechaMaximaHoy]],
      fin: ['', [Validators.required, this.fechaMaximaHoy]],
    });
  }

  // Validador personalizado
  fechaMaximaHoy(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;

    const fechaSeleccionada = new Date(control.value + 'T00:00:00');
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    return fechaSeleccionada > hoy ? { fechaFutura: true } : null;
  }

  generarReporte() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();

      // Mensaje específico si es por la fecha
      if (
        this.form.errors?.['fechaFutura'] ||
        this.form.get('inicio')?.hasError('fechaFutura') ||
        this.form.get('fin')?.hasError('fechaFutura')
      ) {
        this.notify.show('No se pueden consultar fechas futuras', 'error');
      } else {
        this.notify.show('Por favor complete todos los filtros correctamente', 'error');
      }
      return;
    }

    this.loading.set(true);
    const { clienteId, inicio, fin } = this.form.value;

    this.movRepo.getReporte(clienteId, inicio, fin).subscribe({
      next: (res) => {
        this.reporte.set(res);
        this.loading.set(false);
        this.notify.show('Reporte generado correctamente', 'success');
      },
      error: (err) => {
        this.loading.set(false);
        this.notify.show(err.error?.message || 'Error al generar reporte', 'error');
      },
    });
  }

  isInvalid(field: string) {
    const control = this.form.get(field);
    return control ? control.invalid && (control.touched || control.dirty) : false;
  }

  descargarPdf() {
    const base64 = this.reporte()?.pdfBase64;
    if (!base64) return;
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: 'application/pdf' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `Estado_Cuenta_${this.reporte()?.cliente}.pdf`;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}
