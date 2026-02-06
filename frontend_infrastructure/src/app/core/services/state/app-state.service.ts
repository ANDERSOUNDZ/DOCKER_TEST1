import { Injectable, signal } from '@angular/core';

export interface PaginationState {
  currentPage: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

/**
 * Función auxiliar para generar un estado de paginación inicial limpio.
 */
const initialPagination = (): PaginationState => ({
  currentPage: 0,
  pageSize: 10,
  totalElements: 0,
  totalPages: 0,
});

@Injectable({ providedIn: 'root' })
export class AppStateService {
  // --- SEÑALES PRIVADAS ---
  private _items = signal<any[]>([]);
  private _loading = signal<boolean>(false);
  private _searchQuery = signal<string>('');

  // Estados de paginación independientes para evitar persistencia cruzada
  private _paginationClientes = signal<PaginationState>(initialPagination());
  private _paginationCuentas = signal<PaginationState>(initialPagination());
  private _paginationMovimientos = signal<PaginationState>(initialPagination());

  // --- SEÑALES PÚBLICAS (Readonly) ---
  public items = this._items.asReadonly();
  public clientes = this._items.asReadonly();
  public isLoading = this._loading.asReadonly();
  public searchQuery = this._searchQuery.asReadonly();
  public paginationMovimientos = this._paginationMovimientos.asReadonly();

  // Exponemos las paginaciones por separado
  public paginationClientes = this._paginationClientes.asReadonly();
  public paginationCuentas = this._paginationCuentas.asReadonly();

  // --- MÉTODOS DE ACTUALIZACIÓN GENÉRICOS ---

  setItems(data: any[]) {
    this._items.set(data);
  }

  setClientes(data: any[]) {
    this._items.set(data);
  }

  setLoading(value: boolean) {
    this._loading.set(value);
  }

  setSearchQuery(query: string) {
    this._searchQuery.set(query);
  }

  // --- MÉTODOS DE PAGINACIÓN ESPECÍFICOS ---

  /**
   * Actualiza la paginación de Clientes
   */
  setPaginationClientes(data: PaginationState) {
    this._paginationClientes.set(data);
  }

  setPageClientes(page: number) {
    this._paginationClientes.update((prev) => ({ ...prev, currentPage: page }));
  }

  /**
   * Actualiza la paginación de Cuentas
   */
  setPaginationCuentas(data: PaginationState) {
    this._paginationCuentas.set(data);
  }

  setPageCuentas(page: number) {
    this._paginationCuentas.update((prev) => ({ ...prev, currentPage: page }));
  }

  /**
   * Actualiza la paginación de Movimientos
   */
  setPaginationMovimientos(data: PaginationState) {
    this._paginationMovimientos.set(data);
  }

  setPageMovimientos(page: number) {
    this._paginationMovimientos.update((prev) => ({ ...prev, currentPage: page }));
  }

  /**
   * Limpia los items y resetea las búsquedas al cambiar de módulo si es necesario
   */
  clearState() {
    this._items.set([]);
    this._searchQuery.set('');
    this._loading.set(false);
  }
}
