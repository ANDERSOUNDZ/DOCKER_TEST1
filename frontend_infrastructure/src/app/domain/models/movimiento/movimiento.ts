import { Cuenta } from '../cuenta/cuenta';

export interface Movimiento {
  id?: number;
  fecha: string;
  tipoMovimiento: string;
  movimiento: number;
  saldoAnterior: number;
  saldo: number;
  cuenta: Cuenta;
}
