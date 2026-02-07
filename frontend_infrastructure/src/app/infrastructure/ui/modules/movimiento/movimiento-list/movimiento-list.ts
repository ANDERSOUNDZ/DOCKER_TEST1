import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { MovimientoRepository } from '../../../../../domain/repository/movimiento/movimiento.repository';
import { AppStateService } from '../../../../../core/services/state/app-state.service';
import { Alert } from '../../../../../core/services/alert/alert';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MovimientoForm } from '../movimiento-form/movimiento-form';

@Component({
  selector: 'app-movimiento-list',
  imports: [CommonModule, FormsModule, MovimientoForm],
  templateUrl: './movimiento-list.html',
  styleUrl: './movimiento-list.scss',
})
export class MovimientoList implements OnInit {
  private movRepo = inject(MovimientoRepository);
  public state = inject(AppStateService); // Usamos el estado global
  private notify = inject(Alert);

  showForm = signal(false);

  // Ya no usamos un signal local para searchQuery, usamos state.searchQuery()

  sortedItems = computed(() => {
    // Tomamos los movimientos directamente del estado global
    return [...this.state.movimientos()];
  });

  ngOnInit(): void {
    // Al iniciar, cargamos usando lo que ya esté en el estado (página y búsqueda)
    this.loadMovimientos(this.state.searchQuery(), this.state.paginationMovimientos().currentPage);
  }

  /**
   * Carga con persistencia de estado
   */
  loadMovimientos(
    search: string = this.state.searchQuery(),
    page: number = this.state.paginationMovimientos().currentPage,
  ) {
    this.state.setLoading(true);

    // Si el término de búsqueda cambió, reseteamos a la página 0
    if (search !== this.state.searchQuery()) {
      page = 0;
    }

    // Guardamos en el estado global para que persista si navegamos a otra ruta
    this.state.setSearchQuery(search);
    this.state.setPageMovimientos(page);

    this.movRepo.getByFilters(null, '', '', page, search).subscribe({
      next: (res: any) => {
        // Importante: Usar setMovimientos (el método específico de tu state)
        const content = res?.data?.content || res?.content || [];
        this.state.setMovimientos(content);

        const p = res.data || res;
        this.state.setPaginationMovimientos({
          currentPage: p.number ?? page,
          pageSize: p.size ?? 10,
          totalElements: p.totalElements ?? content.length,
          totalPages: p.totalPages ?? 1,
        });
        this.state.setLoading(false);
      },
      error: (err) => {
        this.notify.show(err.error?.message || 'Error al cargar movimientos', 'error');
        this.state.setLoading(false);
      },
    });
  }

  onSearch(event: Event): void {
    const element = event.target as HTMLInputElement;

    // Expresión regular: busca todo lo que NO sea un dígito (\D) y lo reemplaza por vacío
    const numericValue = element.value.replace(/\D/g, '');

    // Actualizamos el valor visual del input por si el usuario pegó letras
    element.value = numericValue;

    // Si el valor numérico es diferente al que ya teníamos, disparamos la carga
    if (numericValue !== this.state.searchQuery()) {
      this.loadMovimientos(numericValue, 0);
    }
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.state.paginationMovimientos().totalPages) {
      this.loadMovimientos(this.state.searchQuery(), page);
    }
  }

  openCreate() {
    this.showForm.set(true);
  }
}
