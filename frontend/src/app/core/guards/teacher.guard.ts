import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';

@Injectable({
  providedIn: 'root'
})
export class TeacherGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {}

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    

    // Verificar si el usuario está autenticado
    const isAuthenticated = this.authService.isAuthenticated();
    console.log('🛡 ¿Usuario autenticado?:', isAuthenticated);

    if (!isAuthenticated) {
      this.alertService.createAlert('Debes iniciar sesión para acceder', 'error', false);
      return this.router.createUrlTree(['']);
    }

    // Verificar datos del usuario desde el servicio (memoria)
    const userData = this.authService.getCurrentUserData();
    const userRole = this.authService.getCurrentUserRole();
    const isTeacher = this.authService.isTeacher();

    // Verificar si el usuario es PROFESOR
    if (isTeacher) {
      return true;
    }

    // Si no es profesor, redirigir según su rol
    console.log('🛡 ❌ Acceso denegado - Usuario no es PROFESOR');
    this.alertService.createAlert('No tienes permisos para acceder a esta sección', 'error', false);

    if (userRole === 'ESTUDIANTE') {
      return this.router.createUrlTree(['/home']);
    } else if (userRole === 'AUDITOR') {
      return this.router.createUrlTree(['/home']);
    } else {
      return this.router.createUrlTree(['/home']);
    }
  }
}