import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../../services/alert.service';
import { CRUDService } from '../../../services/crud.service';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../../services/auth.service';
import { UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  isOpen = false;
  currentLang = 'es';

  constructor(public crudService: CRUDService, public authService: AuthService, private alertService: AlertService, public router: Router, public translate: TranslateService) { 
    this.translate.use('es');
  }

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
    this.authService.logout();
  }

  changeLang(lang: string) {
    this.currentLang = lang;
    this.translate.use(lang);
  }

}

