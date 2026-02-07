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

  // ORDEN INVERSO (Último registro primero)
  sortedItems = computed(() => {
    const items = [...this.state.items()];
    const query = this.searchQuery().trim().toLowerCase();

    const filtered = query
      ? items.filter((m) => m['Numero Cuenta']?.toLowerCase().includes(query))
      : items;

    return filtered.reverse(); // Muestra el último movimiento arriba
  });

  ngOnInit(): void {
    this.loadMovimientos();
  }

  loadMovimientos(page: number = 0) {
    this.state.setLoading(true);
    this.state.setPageMovimientos(page);

    this.movRepo.getByFilters(null, '', '', page).subscribe({
      next: (res: any) => {
        const content = res?.data?.content || res?.content || [];
        this.state.setItems(content);

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
    this.searchQuery.set(element.value);
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
