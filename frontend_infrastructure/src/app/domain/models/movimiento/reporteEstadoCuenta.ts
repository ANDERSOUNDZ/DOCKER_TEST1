import { Movimiento } from "./movimiento";

export interface ReporteEstadoCuenta {
  cliente: string;
  rangoFechas: string;
  movimientos: Movimiento[];
  totalCreditos: number;
  totalDebitos: number;
  pdfBase64: string; // El PDF que viene del backend
}