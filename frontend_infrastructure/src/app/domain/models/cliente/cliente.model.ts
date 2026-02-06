export interface Cliente {
  id: number;
  clienteId: string;
  nombre: string;
  identificacion: string;
  direccion: string;
  telefono: string;
  genero: string;
  edad: number;
  estado: boolean;
}

// Para manejar la paginación de Spring Data
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Para manejar tu BaseResponse<T> del backend
export interface BaseResponse<T> {
  message: string;
  data: T;
  status: number;
  timestamp: string;
}
