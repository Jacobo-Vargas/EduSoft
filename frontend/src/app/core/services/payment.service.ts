import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface PaymentResponse {
  init_point: string;
  id: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {


  private url = environment.urlServer + "/payment/mercado";

  constructor(private http: HttpClient) { }

  getInitpoint(courseId: number): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(this.url, {
      params: { courseId: courseId.toString() },
      withCredentials: true
    });
  }

}
