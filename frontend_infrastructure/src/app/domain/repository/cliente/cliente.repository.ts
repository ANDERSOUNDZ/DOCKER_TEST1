import { Observable } from 'rxjs';
import { BaseResponse, Cliente, Page } from '../../models/cliente/cliente.model';

export abstract class ClienteRepository {
  abstract getAll(search: string, page: number, size: number): Observable<BaseResponse<Page<Cliente>>>;
  abstract create(cliente: Partial<Cliente>): Observable<BaseResponse<Cliente>>;
}
