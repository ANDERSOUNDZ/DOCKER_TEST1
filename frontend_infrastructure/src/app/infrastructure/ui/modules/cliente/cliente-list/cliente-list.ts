import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteRepository } from '../../../../../domain/repository/cliente/cliente.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { ClienteForm } from '../cliente-form/cliente-form';

@Component({
  selector: 'app-cliente-list',
  imports: [CommonModule, ClienteForm],
  templateUrl: './cliente-list.html',
  styleUrl: './cliente-list.scss',
})
export class ClienteList implements OnInit {
  private clienteRepo = inject(ClienteRepository);
  public state = inject(AppStateService);

  showForm = false;

  ngOnInit(): void {
    this.loadClientes(this.state.searchQuery());
  }

  loadClientes(
    search: string = this.state.searchQuery(),
    page: number = this.state.pagination().currentPage,
  ): void {
    this.state.setLoading(true);
    this.state.setSearchQuery(search);
    this.state.setPage(page); // Persistimos la página actual

    this.clienteRepo.getAll(search, page, this.state.pagination().pageSize).subscribe({
      next: (res) => {
        this.state.setClientes(res.data.content);

        // Actualizamos los metadatos de paginación con lo que envió Spring
        this.state.setPagination({
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
    this.loadClientes(element.value);
  }

  openForm() {
    this.showForm = true;
  }
  closeForm() {
    this.showForm = false;
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.state.pagination().totalPages) {
      this.loadClientes(this.state.searchQuery(), page);
    }
  }
}
