import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteRepository } from '../../../../../domain/repository/cliente/cliente.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { ClienteForm } from '../cliente-form/cliente-form';
import { Alert } from '../../../../../core/services/alert/alert';

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

  ngOnInit(): void {
    this.loadClientes(this.state.searchQuery());
  }

  loadClientes(
    search: string = this.state.searchQuery(),
    page: number = this.state.paginationClientes().currentPage, // CORREGIDO
  ): void {
    this.state.setLoading(true);
    this.state.setSearchQuery(search);
    this.state.setPageClientes(page); // CORREGIDO

    this.clienteRepo.getAll(search, page, this.state.paginationClientes().pageSize).subscribe({
      // CORREGIDO
      next: (res) => {
        this.state.setClientes(res.data.content);

        this.state.setPaginationClientes({
          // CORREGIDO
          currentPage: res.data.number,
          pageSize: res.data.size,
          totalElements: res.data.totalElements,
          totalPages: res.data.totalPages,
        });
        this.state.setLoading(false);
      },
    });
  }

  onSearch(event: Event): void {
    const element = event.target as HTMLInputElement;
    this.loadClientes(element.value);
  }

  openForm() {
    this.showForm = true;
  }
  closeForm() {
    this.showForm = false;
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.state.paginationClientes().totalPages) { // CORREGIDO
      this.loadClientes(this.state.searchQuery(), page);
    }
  }

  openCreate() {
    this.selectedCliente.set(null); // Limpiamos para que sea "Crear"
    this.showForm = true;
  }

  openEdit(cliente: any) {
    this.selectedCliente.set(cliente); // Cargamos el cliente para "Editar"
    this.showForm = true;
  }

  toggleEstado(cliente: any) {
    // CORRECCIÓN: Si el estado es TRUE, la acción es INACTIVAR. Si es FALSE, la acción es ACTIVAR.
    const seraActivo = !cliente.estado;
    const accion = seraActivo ? 'activar' : 'inactivar';

    const confirmar = confirm(`¿Está seguro de que desea ${accion} al cliente ${cliente.nombre}?`);

    if (!confirmar) return;

    this.state.setLoading(true);

    if (!seraActivo) {
      // CASO: DESACTIVAR (Usamos tu delete que ya funciona y pone estado = false)
      this.clienteRepo.delete(cliente.id).subscribe({
        next: () => this.handleSuccess(`Cliente desactivado correctamente`),
        error: (err: any) => this.handleError(err),
      });
    } else {
      // CASO: ACTIVAR (Usamos PATCH para evitar que el backend pida género, edad, etc.)
      // Enviamos solo el campo 'estado' como espera tu @PatchMapping
      this.clienteRepo.patch(cliente.id, { estado: true }).subscribe({
        next: () => this.handleSuccess(`Cliente activado correctamente`),
        error: (err: any) => this.handleError(err),
      });
    }
  }

  private handleSuccess(message: string) {
    this.notify.show(message, 'success');
    this.loadClientes(); // Refresca la tabla
  }

  private handleError(err: any) {
    this.state.setLoading(false);
    // Captura el mensaje de error del backend si existe
    const msg = err.error?.message || 'Error al procesar la solicitud';
    this.notify.show(typeof msg === 'object' ? 'Error de validación' : msg, 'error');
  }
}
