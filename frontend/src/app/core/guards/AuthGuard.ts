import { Injectable } from "@angular/core";
import { CanActivate, Router, UrlTree } from "@angular/router";
import { Observable, of, map } from "rxjs";
import { AlertService } from "../services/alert.service";
import { AuthService } from "../services/auth.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {}

  canActivate(): Observable<boolean | UrlTree> {
    if (this.authService.isAuthenticated() && this.authService.getCurrentUserData()) {
      return of(true);
    }

    return this.authService.initAuthState().pipe(
      map(isAuthenticated => {
        if (isAuthenticated) {
          return true;
        } else {
          this.alertService.createAlert('Debes iniciar sesión para acceder', 'error', false);
          return this.router.createUrlTree(['']);
        }
      })
    );
  }
}
