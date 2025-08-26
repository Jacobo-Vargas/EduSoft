import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-send-email',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './send-code-email.html',
  styleUrl: './send-code-email.css'
})
export class SendEmail {

  userForm!: FormGroup;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword = false;
  showSuccessMessage = false;
  showTermsModal = false;   // por si lo usas en el HTML
  formSubmitted = false;
  isLoading = false;
  errorMsg = '';
  showRecoverPassword = false;
  isCodeSent = false;  // Nueva variable para gestionar el cambio de campos
  // Almacenamos el código enviado en el frontend para verificarlo
  generatedCode: string = '';
    // Inyecta el Router

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router 
  ) {
    // Construcción del formulario
    this.userForm = this.fb.group({
      username: ['', [Validators.required]],
       code: ['']  // Campo para el código de verificación
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
  this.formSubmitted = true;
  this.errorMsg = '';

  if (this.userForm.invalid) {
    // Marca todos los controles como tocados para mostrar errores
    Object.values(this.userForm.controls).forEach(c => c.markAsTouched());
    return;
  }

  const formData = this.userForm.value;  // Obtiene los datos del formulario
  this.isLoading = true;

  if (this.isCodeSent) {
    console.log('Verificando código:', formData.code , 'contra', this.generatedCode);
    // Guardar el username en el localStorage
      localStorage.setItem('username', formData.username);
    // El código ya ha sido enviado, ahora lo verificamos
     if (String(formData.code) === String(this.generatedCode)) {
      // Si el código ingresado es correcto
      this.showSuccessMessage = true;
      this.isLoading = false;
      this.userForm.reset();
      console.log('Código verificado correctamente');
        this.router.navigate(['/recover-password']);  // Navega al componente 'RecoverPassword'
      setTimeout(() => this.showSuccessMessage = false, 5000);  // Mensaje de éxito temporal
    } else {
      // Si el código es incorrecto
      this.errorMsg = 'El código ingresado es incorrecto';
      this.isLoading = false;
    }
  } else {
    // Si el código no ha sido enviado, enviamos una solicitud para obtener el código
    const requestData = { username: formData.username };
    this.auth.startReset(requestData).subscribe({
      next: (response) => {
        // Si el código fue enviado con éxito
        this.showSuccessMessage = true;
        this.isCodeSent = true;  // Marcamos que el código fue enviado
        this.generatedCode = response,// Guardamos el código recibido del backend
        this.userForm.addControl('code', this.fb.control('', [Validators.required]));  // Añadimos el control 'code' al formulario
        this.isLoading = false;
         // **Navegar al componente RecoverPassword**
        setTimeout(() => this.showSuccessMessage = false, 5000);  // Mensaje de éxito temporal
      },
      error: (err) => {
        // Manejo de error
        this.errorMsg = err?.error?.message || 'Error inesperado';
        this.isLoading = false;
      }
    });
    }
  }

  // (Opcional) si usas modal de términos en el HTML
  openTermsModal() { this.showTermsModal = true; }
  closeTermsModal() { this.showTermsModal = false; }
}
