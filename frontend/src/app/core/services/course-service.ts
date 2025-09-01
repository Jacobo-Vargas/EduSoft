import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

const API = environment.urlServer;
export interface AuthResponseDTO {
  accessToken: string;
}
export interface CategorieRequestDTO {
  name: string;
  description?: string;
}
@Injectable({
  providedIn: 'root'
})
export class CourseService {
    constructor(private http: HttpClient) {}  
  createCategorie(body: CategorieRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${API}/categories`, body, { withCredentials: true }).pipe(
    map(res => res)
    );
 }
}
