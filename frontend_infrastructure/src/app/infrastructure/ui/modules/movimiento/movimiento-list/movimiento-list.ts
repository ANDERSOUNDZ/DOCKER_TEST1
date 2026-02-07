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
  public state = inject(AppStateService);
  private notify = inject(Alert);

  showForm = signal(false);
  searchQuery = signal<string>('');

  // Ahora solo se encarga de mostrar lo que hay en el estado
  sortedItems = computed(() => {
    return [...this.state.items()]; // El backend ya devuelve la lista filtrada
  });

  ngOnInit(): void {
    this.loadMovimientos();
  }

  // Modificado para aceptar el término de búsqueda
  loadMovimientos(page: number = 0) {
    this.state.setLoading(true);
    this.state.setPageMovimientos(page);
    
    const currentSearch = this.searchQuery();

    this.movRepo.getByFilters(null, '', '', page, currentSearch).subscribe({
      next: (res: any) => {
        const content = res?.content || [];
        this.state.setItems(content);

        this.state.setPaginationMovimientos({
          currentPage: res.number ?? page,
          pageSize: res.size ?? 10,
          totalElements: res.totalElements ?? content.length,
          totalPages: res.totalPages ?? 1,
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
    this.searchQuery.set(element.value);
    
    // Al buscar, siempre volvemos a la página 0
    this.loadMovimientos(0);
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.state.paginationMovimientos().totalPages) {
      this.loadMovimientos(page);
    }
  }

  openCreate() {
    this.showForm.set(true);
  }
}
