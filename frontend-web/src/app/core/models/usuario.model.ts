import { Rol } from './auth.model';

export interface Usuario {
  id: number;
  nombres: string;
  apellidos: string;
  documentoIdentidad: string;
  email: string;
  rol: Rol;
  fechaCreacion: string;
  activo: boolean;
}

export interface UsuarioRequest {
  nombres: string;
  apellidos: string;
  documentoIdentidad: string;
  email: string;
  password: string;
  rol: Rol;
}
