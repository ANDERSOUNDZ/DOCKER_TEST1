import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteRepository } from '../../../../../domain/repository/cliente/cliente.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { ClienteForm } from "../cliente-form/cliente-form";

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

  loadClientes(search: string = ''): void {
    this.state.setLoading(true);
    this.state.setSearchQuery(search);

    this.clienteRepo.getAll(search, 0, 10).subscribe({
      next: (res) => {
        this.state.setClientes(res.data.content);
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
}
