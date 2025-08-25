import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

// Ajusta esto a tu environment/proxy:
const API = 'http://localhost:8080/api/auth';


export interface AuthResponseDTO {
  accessToken: string;
}


export interface LoginRequestDTO {
  username: string;
  password?: string;
  code?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  /** POST /api/auth/login */
  login(body: LoginRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${API}/login`, body).pipe(
      map(res => res)
    );
  }

  /**
   * Enviar código para recuperar contraseña.
   * Ajusta el path a lo que tengas en el backend:
   * - Recomendado: POST /api/auth/search-user
   * - Si dejaste el antiguo: POST /api/auth/searchUser
   */
  startReset(body: { username: string }): Observable<any> {
    return this.http.post(`${API}/search-user`, body);
    // return this.http.post(`${API}/searchUser`, body); // <- usa este si tu endpoint es camello
  }

  /**
   * Confirmar cambio de contraseña con código.
   * Ajusta el path:
   * - Recomendado: POST /api/auth/recover-password
   * - Antiguo: POST /api/auth/recoverPassword
   */
  confirmReset(body: { username: string; code: string; password: string }): Observable<any> {
    return this.http.post(`${API}/recover-password`, body);
    // return this.http.post(`${API}/recoverPassword`, body); // <- usa este si tu endpoint es camello
  }

  // --- Helpers opcionales ---
  // get token(): string | null { return localStorage.getItem('token'); }
  // logout(): void { localStorage.removeItem('token'); }
}
