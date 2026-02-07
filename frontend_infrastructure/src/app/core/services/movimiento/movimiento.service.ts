import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { API_URL } from '../../providers/api-url.token';
import { MovimientoRepository } from '../../../domain/repository/movimiento/movimiento.repository';
import { Movimiento } from '../../../domain/models/movimiento/movimiento';

@Injectable({ providedIn: 'root' })
export class MovimientoService implements MovimientoRepository {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);
  private endpoint = `${this.apiUrl}/movimientos`;

  /**
   * Crea un nuevo movimiento (Depósito/Retiro)
   */
  create(movimiento: Partial<any>): Observable<Movimiento> {
    return this.http.post<any>(this.endpoint, movimiento).pipe(map((res) => res.data));
  }

  /**
   * Genera el reporte de estado de cuenta
   */
  getReporte(clienteId: number, inicio: string, fin: string): Observable<any> {
    const params = new HttpParams()
      .set('clienteId', clienteId.toString())
      .set('inicio', inicio)
      .set('fin', fin);

    return this.http.get<any>(`${this.endpoint}/reporte`, { params }).pipe(map((res) => res.data));
  }

  /**
   * Reversa un movimiento por su ID
   */
  reverse(id: number): Observable<Movimiento> {
    return this.http.post<any>(`${this.endpoint}/reversar/${id}`, {}).pipe(map((res) => res.data));
  }

  getByFilters(
    clienteId: number | null,
    fechaInicio?: string,
    fechaFin?: string,
    page: number = 0,
    search?: string, // <--- Recibir parámetro
  ): Observable<any> {
    let params = new HttpParams().set('page', page.toString()).set('size', '10');

    if (clienteId) params = params.set('cliente', clienteId.toString());
    if (fechaInicio) params = params.set('fechaInicio', fechaInicio);
    if (fechaFin) params = params.set('fechaFin', fechaFin);
    if (search) params = params.set('search', search); // <--- Enviar al Backend

    return this.http.get<any>(this.endpoint, { params }).pipe(map((res) => res.data));
  }
}
