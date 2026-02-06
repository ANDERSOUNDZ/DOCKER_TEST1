import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../../../core/providers/api-url.token'; // Importamos el token
import { ClienteRepository } from '../../../domain/repository/cliente/cliente.repository';
import { BaseResponse, Cliente, Page } from '../../../domain/models/cliente/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClienteService implements ClienteRepository {
  private http = inject(HttpClient);
  private baseUrl = inject(API_URL);

  private readonly endpoint = `${this.baseUrl}/clientes`;

  getAll(
    search: string = '',
    page: number = 0,
    size: number = 10,
  ): Observable<BaseResponse<Page<Cliente>>> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (search) params = params.set('search', search);

    return this.http.get<BaseResponse<Page<Cliente>>>(this.endpoint, { params });
  }
  create(cliente: Partial<Cliente>): Observable<BaseResponse<Cliente>> {
    return this.http.post<BaseResponse<Cliente>>(this.endpoint, cliente);
  }
  update(id: number, cliente: Partial<Cliente>): Observable<BaseResponse<Cliente>> {
    return this.http.put<BaseResponse<Cliente>>(`${this.endpoint}/${id}`, cliente);
  }

  patch(id: number, updates: Map<string, any> | any): Observable<BaseResponse<Cliente>> {
  return this.http.patch<BaseResponse<Cliente>>(`${this.endpoint}/${id}`, updates);
}

  delete(id: number): Observable<BaseResponse<void>> {
    return this.http.delete<BaseResponse<void>>(`${this.endpoint}/${id}`);
  }
}
