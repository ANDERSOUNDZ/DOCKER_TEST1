import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MovimientoRepository } from '../../../../../domain/repository/movimiento/movimiento.repository';
import { Alert } from '../../../../../core/services/alert/alert';
import { ReporteEstadoCuenta } from '../../../../../domain/models/movimiento/reporteEstadoCuenta';

@Component({
  selector: 'app-reporte-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte-create.html',
  styleUrl: './reporte-create.scss',
})
export class ReporteCreate {
  private movRepo = inject(MovimientoRepository);
  private notify = inject(Alert);

  loading = signal(false);
  reporte = signal<ReporteEstadoCuenta | null>(null);

  filtros = {
    clienteId: null,
    inicio: '',
    fin: '',
  };

  generarReporte() {
    if (!this.filtros.clienteId || !this.filtros.inicio || !this.filtros.fin) {
      this.notify.show('Por favor complete todos los filtros', 'error');
      return;
    }

    this.loading.set(true);
    this.movRepo
      .getReporte(this.filtros.clienteId, this.filtros.inicio, this.filtros.fin)
      .subscribe({
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

  descargarPdf() {
    const base64 = this.reporte()?.pdfBase64;
    if (!base64) return;

    // Convertir Base64 a Blob para descarga
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: 'application/pdf' });

    // Crear link de descarga
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `Estado_Cuenta_${this.reporte()?.cliente}.pdf`;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}
