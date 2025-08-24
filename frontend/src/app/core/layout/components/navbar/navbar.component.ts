import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../../services/alert.service';
import { CRUDService } from '../../../services/crud.service';
import { UserInfoService } from '../../../services/user-info.service';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  isOpen = false;

  constructor(public crudService: CRUDService, public userInfo: UserInfoService, private alertService: AlertService, public router: Router) { }

  toggleDropdown() {
    this.isOpen = !this.isOpen;
  }

  selectFlag(flag: any) {
    this.isOpen = false;
  }

  toggleSidebar() {
    this.crudService.sidebarOpen = !this.crudService.sidebarOpen;
  }

  closeSidebar() {
    this.crudService.sidebarOpen = false;
  }

  goToProfile() {
    const isComplete = true;
    if (!isComplete) {
    } else {
      this.router.navigate(['/profile']);
    }
  }


  logout() {
    this.userInfo.logout();
  }

}

