export interface Cuenta {
  id?: number;
  numeroCuenta: string;
  tipoCuenta: 'Ahorros' | 'Corriente' | 'Ahorro Programado' | 'Poliza';
  saldoInicial: number;
  estado: boolean;
  clienteId: number;
  nombreCliente?: string;
  identificacionCliente?: string;
}
