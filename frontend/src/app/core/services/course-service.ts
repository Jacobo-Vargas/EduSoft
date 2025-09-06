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
export interface currentStatusRequestDTO {
  name: string;
  description?: string;
}
export interface auditStatusRequestDTO {
  name: string;
  description?: string;
}
export interface CategorieResponseDTO {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
}
export interface courseRequestDTO {
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
}

@Injectable({
  providedIn: 'root'
})

export class CourseService {
  constructor(private http: HttpClient) { }


  createCategorie(body: CategorieRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${API}/categories`, body, { withCredentials: true }).pipe(
      map(res => res)
    );
  }

  createStatusCourse(body: currentStatusRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${API}/currentStatuses`, body, { withCredentials: true }).pipe(
      map(res => res)
    );
  }

  createStatusAudi(body: auditStatusRequestDTO): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${API}/audiStatus/createAudiStatus`, body, { withCredentials: true }).pipe(
      map(res => res)
    );
  }

  getCategories(): Observable<CategorieResponseDTO[]> {
    return this.http.get<CategorieResponseDTO[]>(`${API}/categories`, { withCredentials: true });
  }

  createCourse(body: FormData): Observable<AuthResponseDTO> {
    return this.http.post<AuthResponseDTO>(`${API}/course/save`, body, { withCredentials: true }).pipe(
      map(res => res)
    );
  }

  deleteCourse(courseId: number): Observable<any> {
    return this.http.delete<any>(`${API}/course/delete/${courseId}`, { withCredentials: true });
  }


  getCoursesByUser(userId: number): Observable<courseResponseDTO[]> {
    return this.http.get<courseResponseDTO[]>(`${API}/course/user/${userId}`, { withCredentials: true });
  }
}