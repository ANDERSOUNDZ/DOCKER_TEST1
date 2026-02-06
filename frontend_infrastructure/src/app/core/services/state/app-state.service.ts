import { Injectable, signal } from '@angular/core';
import { Cliente } from '../../../domain/models/cliente/cliente.model';

@Injectable({ providedIn: 'root' })
export class AppStateService {
  private _clientes = signal<Cliente[]>([]);
  private _loading = signal<boolean>(false);
  private _searchQuery = signal<string>('');

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
}
