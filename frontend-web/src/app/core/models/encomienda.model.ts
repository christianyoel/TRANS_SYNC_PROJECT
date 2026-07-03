export type EstadoEncomienda = 'REGISTRADA' | 'EN_TRANSITO' | 'ENTREGADA' | 'CANCELADA';

export interface Encomienda {
  id: number;
  remitente: string;
  destinatario: string;
  origen: string;
  destino: string;
  peso: number;
  precio: number;
  estado: EstadoEncomienda;
  fecha: string;
}

export interface EncomiendaRequest {
  remitente: string;
  destinatario: string;
  origen: string;
  destino: string;
  peso: number;
  precio: number;
}

/** Forma del PageResponse que devuelve el backend paginado */
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
