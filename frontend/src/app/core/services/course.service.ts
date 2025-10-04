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
  description: string;
  price: number;
  coverUrl: string;
  semester: number;
  priorKnowledge: string;
  estimatedDurationMinutes: number;
  categoryId: number;
  userId: string;
}

export interface courseResponseDTO {
  id: number;
  title: string;
  description: string;
  price: number;
  coverUrl: string;
  semester: number;
  priorKnowledge: string;
  estimatedDurationMinutes: number;

  categoryId: number;
  categoryName: string;

  currentStatusId: number;
  currentStatusName: string;

  auditStatusId: number;
  auditStatusName: string;

  userId: string;
  userName: string;

  createdAt: string;
  updatedAt: string;
  state: string;
}

export interface EnrollmentResponseDTO {
  id: number;
  userId: number;
  userName: string;
  course: CourseResponseDTO; 
  courseTitle: string;
  enrollmentDate: string;
  progressPercentage: number;
  isCompleted: boolean;
  userCourse: string;
}

export interface CourseResponseDTO {
  id: number;
  title: string;
  description: string;
  coverUrl: string;
  estimatedDurationMinutes: number;
  createdAt: string;
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

  searchCourses(title: string): Observable<courseResponseDTO[]> {
    return this.http.get<any[]>(`${API}/search?title=${title}`, { withCredentials: true });
  }
  setStatusAudit(courseId: number): Observable<courseResponseDTO> {
    return this.http.put<courseResponseDTO>(
      `${API}/course/updateCourseAuditStatus/${courseId}`,
      null,  // body vacío
      { withCredentials: true }  // opciones
    );
  }
  updateCourse(courseId: number, body: FormData): Observable<courseResponseDTO> {
    return this.http.put<courseResponseDTO>(`${API}/course/update/${courseId}`, body, { withCredentials: true });
  }

  getVisibleCourses(): Observable<courseResponseDTO[]> {
    return this.http.get<courseResponseDTO[]>(`${API}/course/visible`, {
      withCredentials: true
    });
  }

  enrollToCourse(courseId: number): Observable<any> {
    return this.http.post(
      `${API}/enrollments/enroll`,
      { courseId },
      { withCredentials: true }
    );
  }

  getCoursesStudent(): Observable<any> {
    return this.http.get(
      `${API}/enrollments/finByCoursesUser`,
      { withCredentials: true }
    );
  }

}