import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserIdService {
  private fineractIdSubject = new BehaviorSubject<number | null>(null);

setFineractId(id: number | null) {
  this.fineractIdSubject.next(id);
}

  getFineractId(): Observable<number | null> {
    return this.fineractIdSubject.asObservable();
  }

  // Para obtener el valor actual sincrónicamente (si ya fue seteado)
  getCurrentFineractId(): number | null {
    return this.fineractIdSubject.value;
  }
}
