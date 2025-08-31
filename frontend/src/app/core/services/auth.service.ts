import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

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
    return this.http.post<AuthResponseDTO>(`${API}${environment.urlLogin}`, body, { withCredentials: true }).pipe(
      map(res => res)
    );
  }

startReset(body: { username: string }): Observable<any> {
  return this.http.post(`${API}${environment.urlSendCodeEmail}`, body, { withCredentials: true }).pipe(
    map(response => {
      return response;
    })
  );
}

 // Método para actualizar la contraseña
  updatePassword(body: { username: string; password: string }): Observable<any> {
  return this.http.post(`${API}${environment.urlUpdatePassword}`, body);
}
  /**
   * Confirmar cambio de contraseña con código.
   */
  confirmReset(body: { username: string; code: string; password: string }): Observable<any> {
    return this.http.post(`${API}${environment.urlRecoverPassword}`, body, { withCredentials: true });
  }
  /**
   * Verificar código de cambio de contraseña.
   */
  verifyCode(code: string, body: LoginRequestDTO): Observable<any> {
    // Aquí se realiza la solicitud con el código como parámetro en la URL
    return this.http.post(`${API}/auth/verifyCode/${code}`, body, { withCredentials: true });
  }

  // --- Helpers opcionales ---
  // get token(): string | null { return localStorage.getItem('token'); }
  // logout(): void { localStorage.removeItem('token'); }

}
