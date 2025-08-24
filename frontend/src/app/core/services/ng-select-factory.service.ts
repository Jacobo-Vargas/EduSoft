import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserInfoService } from './user-info.service';
import { NgSelectService } from './ng-select.service';

@Injectable({
  providedIn: 'root'
})
export class NgSelectFactoryService {

  constructor(private http: HttpClient,
    private userInfoService: UserInfoService) { }

  /**
  * Creates and returns a new instance of the NGSelectService.
  *
  * @returns A new instance of the NGSelectService with the provided dependencies.
  */
  create(): NgSelectService {
    return new NgSelectService(this.http, this.userInfoService);
  }
}
