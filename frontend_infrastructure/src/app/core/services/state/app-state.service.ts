import { Injectable, signal } from '@angular/core';
import { Cliente } from '../../../domain/models/cliente/cliente.model';

export interface PaginationState {
  currentPage: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}


@Injectable({ providedIn: 'root' })
export class AppStateService {
  private _clientes = signal<Cliente[]>([]);
  private _loading = signal<boolean>(false);
  private _searchQuery = signal<string>('');

  private _pagination = signal<PaginationState>({
    currentPage: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0
  });

  public pagination = this._pagination.asReadonly();

  public clientes = this._clientes.asReadonly();
  public isLoading = this._loading.asReadonly();
  public searchQuery = this._searchQuery.asReadonly();

  setClientes(data: Cliente[]) {
    this._clientes.set(data);
  }

  setLoading(value: boolean) {
    this._loading.set(value);
  }

  setSearchQuery(query: string) {
    this._searchQuery.set(query);
  }

  setPagination(data: PaginationState) {
    this._pagination.set(data);
  }

  setPage(page: number) {
    this._pagination.update(prev => ({ ...prev, currentPage: page }));
  }
}
