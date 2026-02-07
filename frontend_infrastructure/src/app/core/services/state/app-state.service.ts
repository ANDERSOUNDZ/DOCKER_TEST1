import { Injectable, signal } from '@angular/core';

export interface PaginationState {
  currentPage: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

const initialPagination = (): PaginationState => ({
  currentPage: 0,
  pageSize: 10,
  totalElements: 0,
  totalPages: 0,
});

@Injectable({ providedIn: 'root' })
export class AppStateService {
  private _clientes = signal<any[]>([]);
  private _cuentas = signal<any[]>([]);
  private _movimientos = signal<any[]>([]);

  private _loading = signal<boolean>(false);
  private _searchQuery = signal<string>('');

  private _paginationClientes = signal<PaginationState>(initialPagination());
  private _paginationCuentas = signal<PaginationState>(initialPagination());
  private _paginationMovimientos = signal<PaginationState>(initialPagination());

  public items = this._clientes.asReadonly();
  public clientes = this._clientes.asReadonly();
  public cuentas = this._cuentas.asReadonly();
  public movimientos = this._movimientos.asReadonly();

  public isLoading = this._loading.asReadonly();
  public searchQuery = this._searchQuery.asReadonly();

  public paginationClientes = this._paginationClientes.asReadonly();
  public paginationCuentas = this._paginationCuentas.asReadonly();
  public paginationMovimientos = this._paginationMovimientos.asReadonly();

  setItems(data: any[]) {
    this._clientes.set(data);
  }

  setClientes(data: any[]) {
    this._clientes.set(data);
  }

  setCuentas(data: any[]) {
    this._cuentas.set(data);
  }

  setMovimientos(data: any[]) {
    this._movimientos.set(data);
  }

  setLoading(value: boolean) {
    this._loading.set(value);
  }

  setSearchQuery(query: string) {
    this._searchQuery.set(query);
  }

  setPaginationClientes(data: PaginationState) {
    this._paginationClientes.set(data);
  }

  setPageClientes(page: number) {
    this._paginationClientes.update((prev) => ({ ...prev, currentPage: page }));
  }

  setPaginationCuentas(data: PaginationState) {
    this._paginationCuentas.set(data);
  }

  setPageCuentas(page: number) {
    this._paginationCuentas.update((prev) => ({ ...prev, currentPage: page }));
  }

  setPaginationMovimientos(data: PaginationState) {
    this._paginationMovimientos.set(data);
  }

  setPageMovimientos(page: number) {
    this._paginationMovimientos.update((prev) => ({ ...prev, currentPage: page }));
  }

  clearState() {
    this._clientes.set([]);
    this._cuentas.set([]);
    this._movimientos.set([]);
    this._searchQuery.set('');
    this._loading.set(false);
  }
}
