import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CuentaRepository } from '../../../../../domain/repository/cuenta/cuenta.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { Alert } from '../../../../../core/services/alert/alert';
import { CuentaForm } from '../cuenta-form/cuenta-form';
import { Observable } from 'rxjs';

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
  selectedCuenta = signal<any | null>(null);

  // Señal local para el buscador de cuentas si no está en el AppState
  searchQuery = signal<string>('');

  // LÓGICA DE FILTRADO Y ORDENAMIENTO (Frontend)
  sortedItems = computed(() => {
    // 1. Obtenemos los items del estado
    let items = [...this.state.items()];
    const query = this.searchQuery().toLowerCase();

    // 2. Filtramos localmente por nombre de cliente o número de cuenta
    if (query) {
      items = items.filter(
        (item) =>
          item.numeroCuenta.toLowerCase().includes(query) ||
          item.nombreCliente?.toLowerCase().includes(query),
      );
    }

    // 3. Ordenamos: Activas primero, luego por ID descendente
    return items.sort((a, b) => {
      if (a.estado !== b.estado) return a.estado ? -1 : 1;
      return b.id - a.id;
    });
  });

  ngOnInit(): void {
    this.loadCuentas();
  }

  loadCuentas(page: number = this.state.paginationCuentas().currentPage): void {
    this.state.setLoading(true);
    this.state.setPageCuentas(page);

    this.cuentaRepo.getAll(page, this.state.paginationCuentas().pageSize).subscribe({
      next: (res: any) => {
        // Asumimos que res.data contiene la información de paginación
        const content = res?.data?.content || res?.content || [];
        this.state.setItems(content);

        this.state.setPaginationCuentas({
          currentPage: res?.data?.number ?? res?.number ?? 0,
          pageSize: res?.data?.size ?? res?.size ?? 10,
          totalElements: res?.data?.totalElements ?? res?.totalElements ?? 0,
          totalPages: res?.data?.totalPages ?? res?.totalPages ?? 0,
        });
        this.state.setLoading(false);
      },
      error: () => this.state.setLoading(false),
    });
  }

  onSearch(event: Event): void {
    const element = event.target as HTMLInputElement;
    this.searchQuery.set(element.value);
  }

  // Resto de métodos de UI
  goToPage(page: number): void {
    if (page >= 0 && page < this.state.paginationCuentas().totalPages) {
      this.loadCuentas(page);
    }
  }

  openCreate() {
    this.selectedCuenta.set(null);
    this.showForm = true;
  }

  openEdit(cuenta: any) {
    this.selectedCuenta.set(cuenta);
    this.showForm = true;
  }

  toggleEstado(cuenta: any) {
    const seraActivo = !cuenta.estado;
    const accion = seraActivo ? 'activar' : 'inactivar';

    if (!confirm(`¿Está seguro de que desea ${accion} la cuenta Nº ${cuenta.numeroCuenta}?`))
      return;

    this.state.setLoading(true);
    const request$: Observable<any> = seraActivo
      ? this.cuentaRepo.activar(cuenta.numeroCuenta)
      : this.cuentaRepo.delete(cuenta.id);

    request$.subscribe({
      next: () => this.handleSuccess(`Cuenta ${accion}ada correctamente`),
      error: (err: any) => this.handleError(err),
    });
  }

  private handleSuccess(message: string) {
    this.notify.show(message, 'success');
    this.loadCuentas();
  }

  private handleError(err: any) {
    this.state.setLoading(false);
    const msg = err.error?.message || 'Error al procesar la solicitud';
    this.notify.show(msg, 'error');
  }
}
