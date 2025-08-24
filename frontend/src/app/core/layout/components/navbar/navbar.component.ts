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
    this.userInfo.selectedFlag = flag;
    this.isOpen = false;
  }

  toggleSidebar() {
    this.crudService.sidebarOpen = !this.crudService.sidebarOpen;
  }

  closeSidebar() {
    this.crudService.sidebarOpen = false;
  }

  goToProfile() {
    const isComplete = this.userInfo.fineractId;


    if (!isComplete) {
      this.alertService.createCustomAlert(
        'Por favor completa tu registro para acceder a tu perfil.',
        'custom',
        'assets/svg/icono-informativo.svg',
        'Completar registro'
      ).then((result) => {
        if (result?.value) {
          this.router.navigate(['/complete-registration']);
        }
      });
    } else {
      this.router.navigate(['/profile']);
    }
  }

  validateRedirectionIfFineractId(route: string) {
    if (this.crudService.fineractId) {
      this.crudService.sidebarOpen = false;
      this.router.navigate([route]);
    } else {
      this.alertService.createAlert('Debes registrarte para acceder a tus inversiones', 'info', false);

    }

  }

  logout() {
    this.alertService.createAlert("Sesión cerrada exitosamente", 'success', false);
  }

}

