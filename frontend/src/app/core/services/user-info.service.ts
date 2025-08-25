import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AlertService } from './alert.service';
import { UserIdService } from './user-id.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {
  private token: any;
  public email!: string;
  public firstName!: string;
  public lastName!: string;
  public userName!: string;
  public roles!: string[];
  public loggedIn$ = new BehaviorSubject<boolean>(false);

  constructor(
    private alertService: AlertService,
    private http: HttpClient,
    private userIdService: UserIdService,
    public router: Router
  ) {
    this.alertService.loadJsonData('es');
  }

  async getUserInfo(): Promise<any> {
    try {
      const headers = { request: 'request' };
      const response = await firstValueFrom(
        this.http.get<any>(`${environment.apiUrl}/profile`, { headers })
      );

      if (response.type_message === 'error') {
        this.alertService.createAlert(response.message, response.type_message, false)
          .then(result => {
            if (result.value) {
              this.logout();
            }
          });
        throw new Error('Error en respuesta del backend');
      }
      return response;

    } catch (err) {
      console.error('Error al obtener user info:', err);
      throw err;
    }
  }

  public async getToken() {
    await new Promise<void>((resolve) => {
      const checkToken = () => {
        if (this.token) resolve();
        else setTimeout(checkToken, 30);
      };
      checkToken();
    });
    return this.token;
  }

  public async getRoles() {
    await new Promise<void>((resolve) => {
      const checkRoles = () => {
        if (this.roles) resolve();
        else setTimeout(checkRoles, 30);
      };
      checkRoles();
    });
    return this.roles;
  }

  public checkPermissions(roles: string[]) {
    return roles.some(role => this.roles?.includes(role));
  }



  public logout() {
    this.http.post('/api/logout', {}, { withCredentials: true }).subscribe({
      next: () => {
      this.loggedIn$.next(false);
      this.router.navigate(['/']);
    },
      error: () => {
      this.alertService.createAlert("Ocurrió un error, recarga e intenta nuevamente.", 'error', false);
    }
  });

  }

  public setToken(token: string) {
    this.token = token;
  }

  public handleError(error: any): Observable<never> {
    return new Observable<never>();
  }

  public isTokenValid() {
    return this
  }

  checkLogin() {
    this.http.get('/api/profile', { withCredentials: true }).subscribe({
      next: () => this.loggedIn$.next(true),
      error: () => this.loggedIn$.next(false)
    });
  }

}
