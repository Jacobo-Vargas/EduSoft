import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

const API = environment.urlServer;
export interface LessonResponseDto {
  id: number;
  name: string; 
  description: string; 
  moduleId: number;
  moduleName: string;
}


@Injectable({
    providedIn: 'root'
})
export class LessonService {

    private apiUrl = `${API}/lessons`;
    constructor(private http: HttpClient) { }

    getLessonsByModule(moduleId: number): Observable<LessonResponseDto[]> {
        return this.http.get<LessonResponseDto[]>(`${this.apiUrl}/module/${moduleId}`);
    }
}
