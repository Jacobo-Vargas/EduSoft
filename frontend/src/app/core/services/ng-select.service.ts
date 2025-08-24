import { Injectable } from '@angular/core';
import { UserInfoService } from './user-info.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {BehaviorSubject, firstValueFrom} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NgSelectService {

  public options!: any[];
  public filterText: BehaviorSubject<string> = new BehaviorSubject<string>(''); // BehaviorSubject for filter text

  constructor(private http: HttpClient, private userInfoService: UserInfoService) { }

  /**
  * Loads options from a specified API endpoint and assigns them to the 'options' property.
  *
  * @param endpoint - The URL of the API endpoint.
  */
  public async loadOptionsExternal(endpoint: string): Promise<void> {
    if (this.options == null) {
      const token = await this.userInfoService.getToken();
      const headers = {
        'Authorization': token,
        'Country': this.userInfoService.country
      };

      const response = await firstValueFrom(this.http.get<any>(environment.apiUrl2 + endpoint, { headers }));
      this.options = response.map((element: any) => element);
      this.filterText.next(''); // Reset the text filter
    }
  }
}
