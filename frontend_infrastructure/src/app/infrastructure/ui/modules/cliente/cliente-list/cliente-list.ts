import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteRepository } from '../../../../../domain/repository/cliente/cliente.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { ClienteForm } from '../cliente-form/cliente-form';
import { Alert } from '../../../../../core/services/alert/alert';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-cliente-list',
  imports: [CommonModule, ClienteForm],
  templateUrl: './cliente-list.html',
  styleUrl: './cliente-list.scss',
})
export class ClienteList implements OnInit {
  private clienteRepo = inject(ClienteRepository);
  private notify = inject(Alert);
  public state = inject(AppStateService);

  showForm = false;
  selectedCliente = signal<any | null>(null);
  lastAffectedId = signal<number | null>(null);

  // Lógica de ordenamiento automática: Activos arriba, Inactivos abajo
  sortedClientes = computed(() => {
    const clientes = [...this.state.clientes()];
    return clientes.sort((a, b) => {
      if (a.estado !== b.estado) return a.estado ? -1 : 1;
      return b.id - a.id; // Dentro del mismo estado, los más nuevos primero
    });
  });

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(
    search: string = this.state.searchQuery(),
    page: number = this.state.paginationClientes().currentPage,
  ): void {
    this.state.setLoading(true);
    if (search !== this.state.searchQuery()) page = 0;

    this.state.setSearchQuery(search);
    this.state.setPageClientes(page);

    this.clienteRepo.getAll(search, page, this.state.paginationClientes().pageSize).subscribe({
      next: (res) => {
        this.state.setClientes(res.data.content);
        this.state.setPaginationClientes({
          currentPage: res.data.number,
          pageSize: res.data.size,
          totalElements: res.data.totalElements,
          totalPages: res.data.totalPages,
        });
        this.state.setLoading(false);
      },
      error: () => this.state.setLoading(false),
    });
  }

  onSearch(event: Event): void {
    const element = event.target as HTMLInputElement;
    this.loadClientes(element.value, 0);
  }

  openCreate() {
    this.selectedCliente.set(null);
    this.showForm = true;
  }

  openEdit(cliente: any) {
    this.selectedCliente.set(cliente);
    this.showForm = true;
  }

  onClienteSaved() {
    this.showForm = false;
    this.loadClientes();
  }

  toggleEstado(cliente: any) {
    const seraActivo = !cliente.estado;
    const accion = seraActivo ? 'activar' : 'inactivar';

    if (!confirm(`¿Está seguro de que desea ${accion} al cliente ${cliente.nombre}?`)) return;

    this.state.setLoading(true);
    const request$: Observable<any> = seraActivo
      ? this.clienteRepo.patch(cliente.id, { estado: true })
      : this.clienteRepo.delete(cliente.id);

    request$.subscribe({
      next: () => {
        this.lastAffectedId.set(cliente.id);
        this.handleSuccess(`Cliente ${accion}ado correctamente`);
        setTimeout(() => this.lastAffectedId.set(null), 3000);
      },
      error: (err: any) => this.handleError(err),
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.state.paginationClientes().totalPages) {
      this.loadClientes(this.state.searchQuery(), page);
    }
  }

  private handleSuccess(message: string) {
    this.notify.show(message, 'success');
    this.loadClientes();
  }

  private handleError(err: any) {
    this.state.setLoading(false);
    const msg = err.error?.message || 'Error al procesar la solicitud';
    this.notify.show(typeof msg === 'object' ? 'Error de validación' : msg, 'error');
  }
}
