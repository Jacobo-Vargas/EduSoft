import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { AlertService } from '../../../services/alert.service';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-navbar-student',
  standalone: false,
  templateUrl: './navbar-student.html',
  styleUrl: './navbar-student.css'
})
export class NavbarStudent {
  constructor( public authService: AuthService, private alertService: AlertService, public router: Router, public translate: TranslateService) { 
    this.translate.use('es');
  }

  logout() {
    this.authService.logout();
  }

}
