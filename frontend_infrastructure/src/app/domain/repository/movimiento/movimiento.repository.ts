import { Observable } from 'rxjs';
import { Movimiento } from '../../models/movimiento/movimiento';

export abstract class MovimientoRepository {
  abstract getByFilters(
    clienteId: number | null, // Permitir null para búsqueda general
    fechaInicio?: string,
    fechaFin?: string,
    page?: number, // <--- AGREGAR ESTO (4to argumento)
  ): Observable<any>; // Cambiamos a any porque ahora devuelve un objeto con paginación (content, totalPages, etc)

  abstract create(movimiento: any): Observable<Movimiento>;

  abstract reverse(id: number): Observable<Movimiento>;

  abstract getReporte(clienteId: number, inicio: string, fin: string): Observable<any>;
}
