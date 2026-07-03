export type Rol = 'ADMIN' | 'COUNTER' | 'CONDUCTOR';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  nombres: string;
  apellidos: string;
  email: string;
  rol: Rol;
}

export interface StoredUser {
  userId: number;
  nombres: string;
  apellidos: string;
  email: string;
  rol: Rol;
}
