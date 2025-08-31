import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

// Ajusta esto a tu environment/proxy:
const API = environment.urlServer;


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
    return this.http.post<AuthResponseDTO>(`${API}/auth/login`, body, { withCredentials: true }).pipe(
      map(res => res)
    );
  }

startReset(body: { username: string }): Observable<any> {
  return this.http.post(`${API}/auth/sendCodeEmail`, body, { withCredentials: true }).pipe(
    map(response => {
      console.log('Código enviado:', response);  // Ver el código aquí
      return response;
    })
  );
}

 // Método para actualizar la contraseña
  updatePassword(body: { username: string; password: string }): Observable<any> {
  console.log('Datos enviados al backend:', body);  // Verifica los datos enviados
  return this.http.post(`${API}/auth/updatePassword`, body);
}

  

  /**
   * Confirmar cambio de contraseña con código.
   */
  confirmReset(body: { username: string; code: string; password: string }): Observable<any> {
    return this.http.post(`${API}/auth/recover-password`, body, { withCredentials: true });
  }

  // --- Helpers opcionales ---
  // get token(): string | null { return localStorage.getItem('token'); }
  // logout(): void { localStorage.removeItem('token'); }

}
