import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface userResponseDTO {
  success: boolean;
  message: string;
  type_message: string;
  additionalData?: any;
  datos: {
    email: string;
    name: string;
    phone: string;
    address: string;
    semester?: number;
    coverUrl?: string;
  };
}
export interface userRequestDTO {
  email: string;
  name: string;
  phone: string;
  address: string;
  semester?: number;
  coverUrl?: string;
}
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private userDataSubject = new BehaviorSubject<userResponseDTO | null>(null);
  userData$ = this.userDataSubject.asObservable();

  constructor(private http: HttpClient) { }

  createUser(userData: any): Observable<any> {
    return this.http.post(`${environment.urlServer}/users/createUser`, userData);
  }

  getInformationUser(): Observable<userResponseDTO> {
    return this.http.get<userResponseDTO>(
      `${environment.urlServer}/users/userInformation`,
      { withCredentials: true }
    );
  }

  putInformationUpdateUser(body: FormData): Observable<any> {
    return this.http.put<userResponseDTO>(
      `${environment.urlServer}/users/userUpdateInformation`,
      body,
      { withCredentials: true }
    );
  }

   setUserData(user: userResponseDTO) {
    this.userDataSubject.next(user); // refresca manualmente
  }


}
