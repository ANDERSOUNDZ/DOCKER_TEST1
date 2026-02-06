import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { API_URL } from '../../providers/api-url.token';
import { CuentaRepository } from '../../../domain/repository/cuenta/cuenta.repository';
import { Cuenta } from '../../../domain/models/cuenta/cuenta';

@Injectable({ providedIn: 'root' })
export class CuentaService implements CuentaRepository {
  private http = inject(HttpClient);
  private apiUrl = inject(API_URL);
  private endpoint = `${this.apiUrl}/cuentas`;

  getAll(page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(this.endpoint, { params }).pipe(
      map((res) => res.data),
    );
  }

  getById(id: number): Observable<Cuenta> {
    return this.http.get<any>(`${this.endpoint}/${id}`).pipe(map((res) => res.data));
  }

  create(cuenta: Partial<Cuenta>): Observable<Cuenta> {
    return this.http.post<any>(this.endpoint, cuenta).pipe(map((res) => res.data));
  }

  update(id: number, cuenta: Partial<Cuenta>): Observable<Cuenta> {
    return this.http.put<any>(`${this.endpoint}/${id}`, cuenta).pipe(map((res) => res.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/${id}`);
  }

  activar(numeroCuenta: string): Observable<Cuenta> {
    return this.http
      .patch<any>(`${this.endpoint}/activar/${numeroCuenta}`, {})
      .pipe(map((res) => res.data));
  }
}
