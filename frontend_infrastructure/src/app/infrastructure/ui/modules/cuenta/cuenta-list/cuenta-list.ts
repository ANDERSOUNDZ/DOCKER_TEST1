import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CuentaRepository } from '../../../../../domain/repository/cuenta/cuenta.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { Alert } from '../../../../../core/services/alert/alert';
import { Cuenta } from '../../../../../domain/models/cuenta/cuenta';
import { CuentaForm } from '../cuenta-form/cuenta-form';

@Component({
  selector: 'app-cuenta-list',
  standalone: true,
  imports: [CommonModule, CuentaForm],
  templateUrl: './cuenta-list.html',
  styleUrl: './cuenta-list.scss',
})
export class CuentaList implements OnInit {
  private cuentaRepo = inject(CuentaRepository);
  private notify = inject(Alert);
  public state = inject(AppStateService);

  showForm = false;
  selectedCuenta = signal<Cuenta | null>(null);

  ngOnInit(): void {
    this.loadCuentas();
  }

  loadCuentas(page: number = this.state.paginationCuentas().currentPage): void {
    // CORREGIDO
    this.state.setLoading(true);
    this.state.setPageCuentas(page); // CORREGIDO

    this.cuentaRepo.getAll(page, this.state.paginationCuentas().pageSize).subscribe({
      // CORREGIDO
      next: (res: any) => {
        const content = res?.content || [];
        this.state.setItems(content);

        this.state.setPaginationCuentas({
          // CORREGIDO
          currentPage: res?.number ?? 0,
          pageSize: res?.size ?? 10,
          totalElements: res?.totalElements ?? 0,
          totalPages: res?.totalPages ?? 0,
        });
        this.state.setLoading(false);
      },
      error: () => this.state.setLoading(false),
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.state.paginationCuentas().totalPages) {
      // CORREGIDO
      this.loadCuentas(page);
    }
  }

  openCreate() {
    this.selectedCuenta.set(null);
    this.showForm = true;
  }

  openEdit(cuenta: Cuenta) {
    this.selectedCuenta.set(cuenta);
    this.showForm = true;
  }

  toggleEstado(cuenta: Cuenta) {
    const seraActivo = !cuenta.estado;
    const accion = seraActivo ? 'activar' : 'inactivar';
    const confirmar = confirm(
      `¿Está seguro de que desea ${accion} la cuenta Nº ${cuenta.numeroCuenta}?`,
    );

    if (!confirmar) return;

    this.state.setLoading(true);

    if (!seraActivo) {
      this.cuentaRepo.delete(cuenta.id!).subscribe({
        next: () => this.handleSuccess('Cuenta desactivada correctamente'),
        error: (err: any) => this.handleError(err),
      });
    } else {
      this.cuentaRepo.activar(cuenta.numeroCuenta).subscribe({
        next: () => this.handleSuccess('Cuenta activada correctamente'),
        error: (err: any) => this.handleError(err),
      });
    }
  }

  private handleSuccess(message: string) {
    this.notify.show(message, 'success');
    this.loadCuentas();
  }

  private handleError(err: any) {
    this.state.setLoading(false);
    const msg = err.error?.message || 'Error al procesar la solicitud';
    this.notify.show(typeof msg === 'object' ? 'Error de validación' : msg, 'error');
  }
}
