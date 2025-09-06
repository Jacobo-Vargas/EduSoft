import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

const API = environment.urlServer;
export interface ModuleResponseDto {
  id: number;
  name: string;
  description: string;
  courseId: number;
  displayOrder: number;
  isVisible: boolean;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ModuleService {

  private apiUrl = `${API}/modules`;

  constructor(private http: HttpClient) { }

  getModulesByCourse(courseId: number): Observable<ModuleResponseDto[]> {
    const url = `${this.apiUrl}/course/${courseId}`;
    console.log('🔗 URL completa:', url);

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      withCredentials: true
    };

    return this.http.get<ModuleResponseDto[]>(url, httpOptions);
  }

  createModule(payload: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      withCredentials: true
    };

    return this.http.post(`${this.apiUrl}`, payload, httpOptions);
  }

  deleteModule(moduleId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${moduleId}`, { withCredentials: true });
  }

  

}