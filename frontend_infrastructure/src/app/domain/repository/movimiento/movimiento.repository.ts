import { Observable } from 'rxjs';
import { Movimiento } from '../../models/movimiento/movimiento';

export abstract class MovimientoRepository {
  abstract getByFilters(
    clienteId: number | null,
    fechaInicio?: string,
    fechaFin?: string,
    page?: number,
    search?: string, // <--- Agregar esto como 5to parámetro
  ): Observable<any>;// Cambiamos a any porque ahora devuelve un objeto con paginación (content, totalPages, etc)

  abstract create(movimiento: any): Observable<Movimiento>;

  abstract reverse(id: number): Observable<Movimiento>;

  abstract getReporte(clienteId: number, inicio: string, fin: string): Observable<any>;
}
