import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CourseService } from './course-service';

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
export interface CategorieResponseDTO {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
}
export interface courseResponseDTO {
  id: number;
  title: string;
  description: String;
  price: number;
  coverUrl: String;
  semester: number;
  priorKnowledge: String;
  estimatedDurationMinutes: number;
  categoryId: number;
  userId: number;
  auditStatusName: string;
}

@Injectable({
  providedIn: 'root'
})
export class ModuleService {


  private apiUrl = `${API}/modules`;

  constructor(private http: HttpClient, private courseService: CourseService,) { }



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

  getCategories(): Observable<CategorieResponseDTO[]> {
    return this.http.get<CategorieResponseDTO[]>(`${API}/categories`, { withCredentials: true });
  }
  getCourseById(id: number): Observable<courseResponseDTO> {
  return this.http.get<courseResponseDTO>(`${API}/course/${id}`, { withCredentials: true });
}

}