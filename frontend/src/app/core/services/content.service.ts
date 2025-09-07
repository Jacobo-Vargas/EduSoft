import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

const API = environment.urlServer;

export interface ContentResponseDto {
  id: number;
  title: string;
  description: string;
  fileUrl: string;
  fileType: string;
  lessonId: number;
  lessonName: string;
  courseId: number;
  courseName: string;
  displayOrder: number;
  lifecycleStatus: string;
  isVisible: boolean;
  createdAt: string;
  updatedAt: string;
}

@Injectable({ providedIn: 'root' })
export class ContentService {
  private apiUrl = `${API}/contents`;

  constructor(private http: HttpClient) {}

  getContentsByLesson(lessonId: number): Observable<ContentResponseDto[]> {
    return this.http.get<ContentResponseDto[]>(
      `${this.apiUrl}/lesson/${lessonId}`,
      { withCredentials: true }
    );
  }

  deleteContent(contentId: number): Observable<any> {
    return this.http.delete(
      `${this.apiUrl}/${contentId}`,
      { withCredentials: true }
    );
  }

  createContent(dto: any, file?: File): Observable<ContentResponseDto> {
    const formData = new FormData();

    // Convertimos el DTO a Blob JSON
    formData.append(
      'content',
      new Blob([JSON.stringify(dto)], { type: 'application/json' })
    );

    // Archivo opcional
    if (file) {
      formData.append('file', file, file.name);
    }

    return this.http.post<ContentResponseDto>(
      this.apiUrl,
      formData,
      { withCredentials: true }
    );
  }
}
