import { Component, OnDestroy } from '@angular/core';
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
export class LoginComponent implements OnDestroy {
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

    const loginSub = this.auth.login(formData).subscribe({
      next: (response) => {
        console.log("✅ Respuesta cruda del backend:", response);
        this.showSuccessMessage = true;
        this.userForm.reset();
        this.formSubmitted = false;
        this.isLoading = false;

        // Ahora obtenemos los datos directamente del servicio
        const userData = this.auth.getCurrentUserData();
        console.log("📌 UserData guardado en AuthService:", userData);
        const userRole = this.auth.getCurrentUserRole();
        console.log("📌 Rol detectado en frontend:", userRole);
        // Redirigir según el rol
        this.redirectUserByRole(userRole, userData);

        setTimeout(() => this.showSuccessMessage = false, 5000);
      },
      error: (err) => {
        this.handleLoginError(err);
      }
    });

    this.subscriptions.push(loginSub);
  }

  private redirectUserByRole(userRole: string | null, userData: UserData | null): void {
    if (userRole === 'PROFESOR') {
      console.log('✅ Redirigiendo a /teacher');
      // Pasar datos del usuario como state en la navegación
      this.router.navigate(['/teacher'], { 
        state: { userData: userData }
      });
      this.alertService.createAlert('Bienvenido, Profesor', "success", false);
    } else if (userRole === 'ESTUDIANTE') {
      this.router.navigate(['/student'], { 
        state: { userData: userData }
      });
      this.alertService.createAlert('Bienvenido, Estudiante', "success", false);
    } else if (userRole === 'AUDITOR') {
      this.router.navigate(['/auditor'], { 
        state: { userData: userData }
      });
      this.alertService.createAlert('Bienvenido, Auditor', "success", false);
    } else {
      console.log('⚠ Rol desconocido, redirigiendo a /dashboard');
      this.router.navigate(['/dashboard'], { 
        state: { userData: userData }
      });
      this.alertService.createAlert('Inicio de sesión exitoso', "success", false);
    }
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

  // Método para verificar acceso específico a teacher (opcional)
  checkTeacherAccess(): boolean {
    return this.auth.isTeacher();
  }

  // (Opcional) si usas modal de términos en el HTML
  openTermsModal() {
    this.showTermsModal = true;
  }

  closeTermsModal() {
    this.showTermsModal = false;
  }
}