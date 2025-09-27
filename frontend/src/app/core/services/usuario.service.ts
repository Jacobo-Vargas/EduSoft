import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
  };
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  constructor(private http: HttpClient) {}

  createUser(userData: any): Observable<any> {
    return this.http.post(`${environment.urlServer}/users/createUser`, userData);
  }

 getInformationUser(): Observable<userResponseDTO> {
  return this.http.get<userResponseDTO>(
    `${environment.urlServer}/users/userInformation`,
    { withCredentials: true }
  );
}

}
