import { Injectable } from "@angular/core";
import { CanActivate, Router, UrlTree } from "@angular/router";
import { AlertService } from "../services/alert.service";
import { AuthService } from "../services/auth.service";

@Injectable({ providedIn: 'root' })
export class TeacherGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {}

  canActivate(): boolean | UrlTree {
    if (this.authService.getCurrentUserRole() === 'PROFESOR') {
      return true;
    }
    this.alertService.createAlert('No tienes permisos para acceder a esta sección', 'error', false);
    return this.router.createUrlTree(['/home']);
  }
}
