import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { RecoverPassword } from "../recover-password/recover-password";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  styleUrls: ['./login.css']
})
export class LoginComponent {

  userForm!: FormGroup;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword = false;
  showSuccessMessage = false;
  showTermsModal = false;   // por si lo usas en el HTML
  formSubmitted = false;
  isLoading = false;
  errorMsg = '';
  showRecoverPassword = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService
  ) {
    // Construcción del formulario
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      // code: [''] // si luego usas recuperación, déjalo opcional
    });
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

    const formData = this.userForm.value; // { username, password }
    this.isLoading = true;

    console.log("esta intentando entrar")
    this.auth.login(formData).subscribe({
      next: () => {
        console.log("entro")
        this.showSuccessMessage = true;
        this.userForm.reset();
        this.formSubmitted = false;
        this.isLoading = false;
        setTimeout(() => this.showSuccessMessage = false, 5000);
      },
      error: (err) => {
        const status = err?.status;
        const msg = err?.error?.message || 'Error inesperado';
        if (status === 401) this.errorMsg = 'Credenciales inválidas';
        else if (status === 403) this.errorMsg = 'Cuenta no verificada';
        else if (status === 503) this.errorMsg = 'Servicio no disponible. Inténtalo más tarde.';
        else this.errorMsg = msg;
        this.isLoading = false;
      }
    });
  }

  // (Opcional) si usas modal de términos en el HTML
  openTermsModal() { this.showTermsModal = true; }
  closeTermsModal() { this.showTermsModal = false; }



  
}

