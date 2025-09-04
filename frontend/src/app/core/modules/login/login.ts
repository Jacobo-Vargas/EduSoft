import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService, UserData } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  standalone: false,
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnDestroy, OnInit  {
  userForm!: FormGroup;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword = false;
  showSuccessMessage = false;
  showTermsModal = false;
  formSubmitted = false;
  isLoading = false;
  errorMsg = '';
  showRecoverPassword = false;
  
  private subscriptions: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {
    // Construcción del formulario
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  ngOnInit(): void {
    if (this.auth.isAuthenticated() && this.auth.getCurrentUserData()) {
      this.auth.redirectByRole();
    } else {
      this.auth.initAuthState().subscribe((isAuth) => {
        if (isAuth) {
          this.auth.redirectByRole();
        }
      });
    }
  }

  ngOnDestroy(): void {
    // Limpiar suscripciones para evitar memory leaks
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  // Acceso rápido a los controles en el template
  get f() { return this.userForm.controls; }

  // Accesibilidad (A-/A+)
  setFontSize(size: 'small' | 'normal' | 'large') {
    this.currentSize = size;
  }

  // Ojo de la contraseña
  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  // Enviar login
  onSubmit(): void {
    this.formSubmitted = true;
    this.errorMsg = '';

    if (this.userForm.invalid) {
      // Marca todos como tocados para mostrar errores
      Object.values(this.userForm.controls).forEach(c => c.markAsTouched());
      return;
    }

    const formData = this.userForm.value;
    this.isLoading = true;

    this.auth.login(formData).subscribe({
      next: (response) => {
        this.showSuccessMessage = true;
        this.userForm.reset();
        this.formSubmitted = false;
        this.isLoading = false;
        this.auth.redirectByRole();
      },
      error: (err) => {
        this.handleLoginError(err);
      }
    });
    

  }

  private handleLoginError(err: any): void {
    const status = err?.status;
    const msg = err?.error?.message || 'Error inesperado';
    
    if (status === 401) {
      this.errorMsg = 'Credenciales inválidas';
    } else if (status === 403) {
      this.errorMsg = 'Cuenta no verificada';
    } else if (status === 503) {
      this.errorMsg = 'Servicio no disponible. Inténtalo más tarde.';
    } else {
      this.errorMsg = msg;
    }
    
    this.isLoading = false;
    this.alertService.createAlert(this.errorMsg, "error", false);
    
    // Limpiar datos en caso de error
    this.auth.clearUserData();
  }

  openTermsModal() {
    this.showTermsModal = true;
  }

  closeTermsModal() {
    this.showTermsModal = false;
  }
}