import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../../services/alert.service';
import { CRUDService } from '../../../services/crud.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {
  @Input() crudService!: CRUDService;

  constructor(public router: Router, private alertService: AlertService, public authService: AuthService){

  }

  closeSidebar() {
    this.crudService.sidebarOpen = false;
  }
  
  
}
