export interface ReporteEstadoCuenta {
  cliente: string;
  rangoFechas: string;
  movimientos: MovimientoReporteItem[];
  totalCreditos: number;
  totalDebitos: number;
  pdfBase64: string;
}

export interface MovimientoReporteItem {
  Fecha: string;
  Cliente: string;
  'Numero Cuenta': string;
  Tipo: string;
  'Saldo Inicial': number;
  Estado: boolean;
  Movimiento: number;
  'Saldo Disponible': number;
}
