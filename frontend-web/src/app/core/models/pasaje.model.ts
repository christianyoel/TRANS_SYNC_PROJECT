export interface Pasaje {
  id: number;
  numeroViaje: string;
  nombreCompleto: string;
  origen: string;
  destino: string;
  asiento: number;
  fechaViaje: string;
  documentoPasajero: string;
  precio: number;
}

export interface VenderPasajeRequest {
  numeroViaje: string;
  asiento: number;
  documentoPasajero: string;
  nombrePasajero: string;
}

export interface AsientosDisponibles {
  numeroViaje: string;
  capacidad: number;
  ocupados: number[];
  disponibles: number[];
}
