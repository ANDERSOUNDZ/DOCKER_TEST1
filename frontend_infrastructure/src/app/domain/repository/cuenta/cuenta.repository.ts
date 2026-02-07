import { Observable } from 'rxjs';
import { Cuenta } from '../../models/cuenta/cuenta';

export abstract class CuentaRepository {
  abstract getAll(page: number, size: number, search?: string): Observable<any>;
  abstract create(cuenta: Partial<Cuenta>): Observable<Cuenta>;
  abstract update(id: number, cuenta: Partial<Cuenta>): Observable<Cuenta>;
  abstract delete(id: number): Observable<void>;
  abstract activar(numeroCuenta: string): Observable<Cuenta>;
}
