export type ViajeEstado = 'ACTIVO' | 'CANCELADO' | 'COMPLETO';

export interface Viaje {
  id: number;
  numeroViaje: string;
  origen: string;
  destino: string;
  fechaSalida: string;
  precio: number;
  capacidad: number;
  estado: ViajeEstado;
  fechaCreacion: string;
}

export interface ViajeRequest {
  numeroViaje: string;
  origen: string;
  destino: string;
  fechaSalida: string;
  precio: number;
  capacidad: number;
}
