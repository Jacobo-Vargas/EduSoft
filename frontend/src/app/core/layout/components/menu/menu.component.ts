import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../../services/alert.service';
import { CRUDService } from '../../../services/crud.service';
import { UserInfoService } from '../../../services/user-info.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {
  @Input() crudService!: CRUDService;

  constructor(public userInfo: UserInfoService , public router: Router,private alertService: AlertService){

  }

  closeSidebar() {
    this.crudService.sidebarOpen = false;
  }
  validateRedirectionIfFineractId(route:string) {
    if (this.crudService.fineractId) {
      this.crudService.sidebarOpen = false;
      this.router.navigate([route]);
    }else{
      this.alertService.createAlert('Debes registrarte para acceder a tus inversiones', 'info', false);

    }

  }
  
}
