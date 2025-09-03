import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-recover-password',
  standalone: false,
  templateUrl: './recover-password.html',
  styleUrl: './recover-password.css'
})
export class RecoverPassword {
  userForm!: FormGroup;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword = false;
  showSuccessMessage = false;
  showTermsModal = false;  
  formSubmitted = false;
  isLoading = false;
  errorMsg = '';
  showRecoverPassword = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    // Construcción del formulario
    this.userForm = this.fb.group({
      Password1: ['', [Validators.required]],
      Password2: ['', [Validators.required, Validators.minLength(8)]],
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
  onSubmit(): void {
    // Verificamos si las contraseñas coinciden
    const formData = this.userForm.value;
    if (formData.Password1 !== formData.Password2) {
      this.errorMsg = 'Las contraseñas no coinciden.';
      return;
    }

    // Recuperamos el 'username' desde localStorage
    const username = localStorage.getItem('username');
    if (!username) {
      this.errorMsg = 'No se encontró el nombre de usuario en el localStorage';
      return;
    }
    this.isLoading = true;
    // Creamos el objeto que enviaremos al backend
    const requestData = { username, password: formData.Password1 };
    // Enviamos la solicitud al backend para actualizar la contraseña
    console.log('Enviando datos de recuperación:', requestData);
    this.auth.updatePassword(requestData).subscribe({
      next: (response) => {
        this.showSuccessMessage = true;
        setTimeout(() => this.showSuccessMessage = false, 5000);
        this.userForm.reset();
        this.isLoading = false;
        this.router.navigate(['/login']);  // Redirige a la página de login
      },
      error: (err) => {
        console.error('Error desde el backend:', err);
        this.errorMsg = err?.error?.message || 'Error inesperado';
        this.isLoading = false;
      }
    });
  }
  // (Opcional) si usas modal de términos en el HTML
  openTermsModal() { this.showTermsModal = true; }
  closeTermsModal() { this.showTermsModal = false; }

}
