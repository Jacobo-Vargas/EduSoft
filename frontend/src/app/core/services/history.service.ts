import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

const API = environment.urlServer;

export interface CourseEventResponseDto {
  eventType: string;
  description: string;
  createdAt: string;
  createdBy: string;
  courseId: number;
  moduleId?: number;
  lessonId?: number;
  contentId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  private apiUrl = `${API}/course-event`;

  constructor(private http: HttpClient) {}

  getCourseHistory(courseId: number): Observable<CourseEventResponseDto[]> {
    return this.http.get<CourseEventResponseDto[]>(
      `${this.apiUrl}/courses/${courseId}/history`,
      { withCredentials: true }
    );
  }
}