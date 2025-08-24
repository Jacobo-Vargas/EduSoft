import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AlertService } from './alert.service';
import { UserIdService } from './user-id.service';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {
  private token: string = '';
  public email!: string;
  public firstName!: string;
  public lastName!: string;
  public userName!: string;
  public officeId!: string | number;
  public officeName!: string;
  public staffId!: number;
  public maritalStatusId!: number;
  public occupation!: string;
  public nationality!: string;
  public roles!: string[];
  public country!: string;
  public fineractId: number | null = null;
  public user_id!: any;
  public phone!: string;
  public business!: string;
  public userInfo: any = {};
  public valuesAttribution: any = {};
  public auth = false;
  public indexFlag: number = 0;
  public tipoCliente!: string;
  public documentNumber!: string;
  public flags = [
    { src: 'https://s3.us-east-2.amazonaws.com/fineract.dev.public/assets/img/chile-flag.png', alt: 'Chile' },
  ];
  public selectedFlag = this.flags[0];

  constructor(
    private alertService: AlertService,
    private http: HttpClient,
    private userIdService: UserIdService
  ) {
    this.alertService.loadJsonData('es');
  }

  async getUserInfo(): Promise<any> {
    try {
      const headers = { request: 'request' };
      const response = await firstValueFrom(
        this.http.get<any>(`${environment.apiUrl}/user-info`, { headers })
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

      const parsedId = this.parseFineractId(response.fineractId);
      if (parsedId !== null) {
        this.userIdService.setFineractId(parsedId);
        localStorage.setItem('fineractId', String(parsedId));
      } else {
        localStorage.removeItem('fineractId');
      }

      this.setUserData(response);
      return response;

    } catch (err) {
      console.error('Error al obtener user info:', err);
      throw err;
    }
  }

  private setUserData(data: any): void {
    console.log(data)
    this.userName = data.userName;
    this.email = data.email;
    this.token = 'data.token.idToken.tokenValue';
    this.roles = data.roles;
    this.officeId = data.officeId;
    this.staffId = data.staffId;
    this.officeName = data.officeName;
    this.firstName = data.firstName;
    this.lastName = data.lastname;
    this.country = data.country;
    this.phone = data.phone;
    this.business = data.business;
    this.documentNumber = data.documentNumber || '';
    this.indexFlag = this.setFlagByCountry(this.country);
    this.selectedFlag = this.flags[this.indexFlag];
    this.user_id = 'data.token.idToken.claims.sub';
    this.tipoCliente = data.tipoCliente || 'retail';
    this.nationality=data.nationality;
    this.occupation=data.occupation;
    this.maritalStatusId = data.maritalStatusId;
    this.fineractId = this.parseFineractId(data.fineractId);
    this.userIdService.setFineractId(this.fineractId);

    this.userInfo = {
      officeId: this.officeId,
      officeName: this.officeName,
      userName: this.userName,
      fineractId: this.fineractId,
      tipoCliente: this.tipoCliente
    };
  }

  private parseFineractId(value: any): number | null {
    if (value === null || value === undefined || value === '' || value === 'null') {
      return null;
    }
    const parsed = Number(value);
    return isNaN(parsed) ? null : parsed;
  }

public async getAsyncUserinfo(): Promise<any> {
  const headers = { 'request': 'request' };
  return firstValueFrom(
    this.http.get<any>(environment.apiUrl + '/user-info', { headers })
      .pipe(catchError((error) => {
        this.handleError(error);
        throw error;
      }))
  ).then(data => {
    this.userName = data.userName;
    this.email = data.email;
    this.token = data.token.idToken.tokenValue;
    this.roles = data.roles;
    this.officeId = data.officeId;
    this.staffId = data.staffId;
    this.officeName = data.officeName;
    this.firstName = data.firstName;
    this.lastName = data.lastname;
    this.country = data.country;
    this.indexFlag = this.setFlagByCountry(this.country);
    this.selectedFlag = this.flags[this.indexFlag];
    this.phone = data.phone;
    this.business = data.business;
    this.tipoCliente= data.tipoCliente;

    this.tipoCliente = data.tipoCliente || 'retail';
    const newFineractId = this.parseFineractId(data.fineractId);
    if (newFineractId) {
      this.fineractId = newFineractId;
      localStorage.setItem('fineractId', String(newFineractId));
      this.userIdService.setFineractId(newFineractId);
    } else {
      // fallback: usar el que estaba en localStorage si no vino en backend
      const stored = localStorage.getItem('fineractId');
      this.fineractId = stored ? Number(stored) : null;
      if (this.fineractId) {
        this.userIdService.setFineractId(this.fineractId);
      }
    }

    this.user_id = data.token.idToken.claims.sub;

    this.userInfo = {
      officeId: this.officeId,
      officeName: this.officeName,
      userName: this.userName,
    };

    return data;
  });
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

  public async getValuesAttribution() {
    await new Promise<void>((resolve) => {
      const checkValuesAttribution = () => {
        if (this.valuesAttribution) resolve();
        else setTimeout(checkValuesAttribution, 30);
      };
      checkValuesAttribution();
    });
    return this.valuesAttribution;
  }

  public getValuesLevelsAttribution() {
    const headers = { 'request': 'request' };
    this.http.get<any>(environment.apiUrl + '/get-values-levels-attribution', { headers })
      .pipe(catchError((error) => this.handleError(error)))
      .subscribe(data => {
        if (data.type_message === 'success') {
          this.valuesAttribution = data.data;
        }
      });
  }

  public logout(avoidEndpoint: boolean = false) {
    const headers = { 'Authorization': this.token };
    const endPoint = environment.urlLogout;
    const redirect = "&post_logout_redirect_uri=" + environment.urlServer;
    localStorage.removeItem('fineractId');
    if (!avoidEndpoint) {
      this.http.get(environment.apiUrl + 'logout', { headers })
    }
    window.location.href = environment.urlServer + "logout";
  }

  public setToken(token: string) {
    this.token = token;
  }

  public handleError(error: any): Observable<never> {
    return new Observable<never>();
  }

  normalizeCountry(name: string | undefined | null): string {
    return name ? name.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim() : '';
  }

  setFlagByCountry(country: string | undefined | null): number {
    const normalizedCountry = this.normalizeCountry(country);
    const index = this.flags.findIndex(flag => this.normalizeCountry(flag.alt) === normalizedCountry);
    this.indexFlag = index !== -1 ? index : this.flags.length - 1;
    return this.indexFlag;
  }

  public async reloadUserInfo(): Promise<void> {
  // Intenta obtener del backend
  try {
    const data = await this.getAsyncUserinfo();

    // Si no llega fineractId, intenta recuperarlo de localStorage
    //
    if (!this.fineractId || this.fineractId === null) {
      const localId = localStorage.getItem('fineractId');
      if (localId) {
        this.fineractId = Number(localId);
        if (this.userInfo) {
          this.userInfo.fineractId = this.fineractId;
        }
      }
    }
  } catch (error) {
    console.error('No fue posible recargar información del usuario:', error);
  }
}

}
