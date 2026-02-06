import { Cuenta } from "../cuenta/cuenta";

export interface Movimiento {
  id?: number;
  fecha: string;
  tipoMovimiento: string; // Faltaba este
  movimiento: number;    // El backend lo mapea como @JsonProperty("movimiento")
  saldoAnterior: number;
  saldo: number;         // Faltaba este
  cuenta: Cuenta;
}
