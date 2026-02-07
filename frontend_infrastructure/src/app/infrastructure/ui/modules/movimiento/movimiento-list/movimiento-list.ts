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

  sortedItems = computed(() => {
    return [...this.state.movimientos()];
  });

  ngOnInit(): void {
    this.loadMovimientos(this.state.searchQuery(), this.state.paginationMovimientos().currentPage);
  }

  loadMovimientos(
    search: string = this.state.searchQuery(),
    page: number = this.state.paginationMovimientos().currentPage,
  ) {
    this.state.setLoading(true);
    if (search !== this.state.searchQuery()) {
      page = 0;
    }

    this.state.setSearchQuery(search);
    this.state.setPageMovimientos(page);

    this.movRepo.getByFilters(null, '', '', page, search).subscribe({
      next: (res: any) => {
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

    const numericValue = element.value.replace(/\D/g, '');

    element.value = numericValue;

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
