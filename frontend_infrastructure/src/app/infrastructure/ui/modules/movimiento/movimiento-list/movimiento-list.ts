import { Component, inject, OnInit, signal } from '@angular/core';
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

  // Estados locales para el formulario y filtros
  showForm = signal(false);
  clienteId = signal<number | null>(null);
  fechaInicio = signal<string>('');
  fechaFin = signal<string>('');

  ngOnInit(): void {
    // Limpiamos el estado previo para evitar mostrar datos de otros módulos
    this.state.clearState();
    // Carga inicial (traerá todos si el backend lo permite o lista vacía)
    this.loadMovimientos();
  }

  /**
   * Carga los movimientos aplicando filtros y paginación
   * @param page Número de página a consultar
   */
  loadMovimientos(page: number = 0) {
    this.state.setLoading(true);

    // Sincronizamos la página actual en el estado global
    this.state.setPageMovimientos(page);

    // Aseguramos que el ID sea numérico o null
    const idParam = this.clienteId() ? Number(this.clienteId()) : null;

    // Llamada al repositorio pasando filtros y página
    // Nota: Asegúrate que tu repositorio acepte el parámetro 'page'
    this.movRepo.getByFilters(idParam, this.fechaInicio(), this.fechaFin(), page).subscribe({
      next: (res: any) => {
        // Manejo flexible de la respuesta (por si viene envuelta o directa)
        const content = res?.content || res || [];
        this.state.setItems(content);

        // Actualizamos la metadata de paginación si el backend la provee
        if (res && res.totalPages !== undefined) {
          this.state.setPaginationMovimientos({
            currentPage: res.number ?? page,
            pageSize: res.size ?? 10,
            totalElements: res.totalElements ?? content.length,
            totalPages: res.totalPages ?? 1,
          });
        }

        this.state.setLoading(false);
      },
      error: (err) => {
        const errorMsg = err.error?.message || 'Error al consultar movimientos';
        this.notify.show(errorMsg, 'error');
        this.state.setLoading(false);
        this.state.setItems([]);
      },
    });
  }

  /**
   * Cambia a una página específica validando los límites
   */
  goToPage(page: number): void {
    const total = this.state.paginationMovimientos().totalPages;
    if (page >= 0 && page < total) {
      this.loadMovimientos(page);
    }
  }

  /**
   * Abre el formulario para registrar una nueva transacción
   */
  openCreate() {
    this.showForm.set(true);
  }
}
