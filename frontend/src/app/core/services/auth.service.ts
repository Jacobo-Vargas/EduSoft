import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';

const API = environment.urlServer;

export interface AuthResponseDTO {
  success: boolean;
  message: string;
  type_message: string;
  additionalData?: any;
  data: {
    id: number;
    userType: string;
    email: string;
    name: string;
  };
}



export interface LoginRequestDTO {
  username: string;
  password?: string;
  code?: string;
}

export interface UserData {
  id: number;
  userType: string;
  email: string;
  name: string;
}



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // BehaviorSubject para manejar el estado del usuario
  private userDataSubject = new BehaviorSubject<UserData | null>(null);
  public userData$ = this.userDataSubject.asObservable();

  // BehaviorSubject para el estado de autenticación
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient) {}

  /** POST /api/auth/login */
  login(body: LoginRequestDTO): Observable<AuthResponseDTO> {
  return this.http.post<AuthResponseDTO>(`${API}/login`, body, {
    withCredentials: true
  }).pipe(
    map(res => {
      if (res.success && res.data) {
  const userData: UserData = {
    id: res.data.id,
    userType: res.data.userType,
    email: res.data.email,
    name: res.data.name
  };
  this.setUserData(userData);
  this.setAuthenticated(true);
}


      return res;
    })
  );
}


  // Método para establecer datos del usuario
  private setUserData(userData: UserData): void {
    this.userDataSubject.next(userData);
  }

  // Método para establecer estado de autenticación
  private setAuthenticated(isAuth: boolean): void {
    this.isAuthenticatedSubject.next(isAuth);
  }

  startReset(body: { username: string }): Observable<any> {
    return this.http.post(`${API}/auth/sendCodeEmail`, body, {
      withCredentials: true
    }).pipe(
      map(response => response)
    );
  }

  updatePassword(body: { username: string; password: string }): Observable<any> {
    return this.http.post(`${API}/auth/updatePassword`, body);
  }

  confirmReset(body: { username: string; code: string; password: string }): Observable<any> {
    return this.http.post(`${API}/auth/recover-password`, body, {
      withCredentials: true
    });
  }

  verifyCode(code: string, body: LoginRequestDTO): Observable<any> {
    return this.http.post(`${API}/auth/verifyCode/${code}`, body, {
      withCredentials: true
    });
  }

  // --- Métodos para obtener datos del usuario (ahora síncronos) ---
  getCurrentUserData(): UserData | null {
    return this.userDataSubject.value;
  }

  getCurrentUserRole(): string | null {
    const userData = this.getCurrentUserData();
    return userData ? userData.userType : null;
  }

  isTeacher(): boolean {
    return this.getCurrentUserRole() === 'PROFESOR';
  }

  isStudent(): boolean {
    return this.getCurrentUserRole() === 'ESTUDIANTE';
  }

  isAuditor(): boolean {
    return this.getCurrentUserRole() === 'AUDITOR';
  }

  // Método para verificar si el usuario está autenticado
  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  logout(): Observable<any> {
    return this.http.post(`${API}/auth/logout`, {}, {
      withCredentials: true
    }).pipe(
      map(res => {
        this.clearUserData();
        return res;
      })
    );
  }

  // Método para limpiar datos (en caso de error)
  clearUserData(): void {
    this.userDataSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  // Método para obtener userData como Observable (para componentes que necesiten reactividad)
  getUserData(): Observable<UserData | null> {
    return this.userData$;
  }

  // Método para obtener estado de autenticación como Observable
  getAuthStatus(): Observable<boolean> {
    return this.isAuthenticated$;
  }

  getCurrentUserId(): number | null {
  return this.getCurrentUserData()?.id || null;
}


}