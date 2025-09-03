import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';
import { RecaptchaComponent } from '../recaptcha/recaptcha.component';
import { AlertService } from '../../services/alert.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-user',
  standalone: false,
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {
  @ViewChild(RecaptchaComponent) recaptchaComponent!: RecaptchaComponent;
  
  userForm: FormGroup;
  captchaToken: string | null = null;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword: boolean = false;
  showSuccessMessage: boolean = false;
  isSubmitting: boolean = false; // Para evitar envíos múltiples

  constructor(private fb: FormBuilder, private usuarioService: UsuarioService, private alertService: AlertService, private router: Router) {
    this.userForm = this.fb.group({
      documentNumber: ['', Validators.required],
      name: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
    });
  }

  ngOnInit() {}

  // Método que recibe el token desde el componente reCAPTCHA
  onToken(token: string) {
    console.log('Token recibido en componente padre:', token ? 'Válido' : 'Vacío/Expirado');
    this.captchaToken = token || null; // Si token está vacío, poner null
    
    if (!token) {
      console.warn('reCAPTCHA expirado o con error');
    }
  }

  // Validador personalizado de contraseña
  passwordStrengthValidator(control: any) {
    const value = control.value;
    if (!value) return null;

    const hasUppercase = /[A-Z]/.test(value);
    const hasLowercase = /[a-z]/.test(value);
    const hasNumber = /\d/.test(value);
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(value);

    return hasUppercase && hasLowercase && hasNumber && hasSpecialChar ? null : { passwordStrength: true };
  }

  setFontSize(size: 'small' | 'normal' | 'large') {
    this.currentSize = size;
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
  if (this.userForm.valid && this.captchaToken) {
    this.isSubmitting = true;

    const userData = {
      ...this.userForm.value,
      captchaToken: this.captchaToken
    };

    this.usuarioService.createUser(userData).subscribe({
      next: (response) => {
        console.log('✅ Usuario registrado exitosamente:', response);
        alert('Usuario registrado exitosamente');
        this.userForm.reset();
        this.resetRecaptcha();
        this.router.navigate(['/login']); 
      },
      error: (error) => {
        console.error('❌ Error al registrar usuario:', error);

        let errorMessage = 'Error al registrar usuario. ';
        if (error.status === 400) {
          errorMessage += 'Datos inválidos o reCAPTCHA inválido.';
        } else if (error.status === 500) {
          errorMessage += 'Error del servidor. Intenta más tarde.';
        } else {
          errorMessage += 'Problema de conexión.';
        }

        this.alertService.createAlert(errorMessage, "error", false);
        this.resetRecaptcha();

        this.isSubmitting = false;
      },
      complete: () => {
        this.isSubmitting = false;
      }
    });
  } else {
    console.warn('❌ Formulario inválido o reCAPTCHA no completado');
    alert('Por favor completa todos los campos requeridos y el reCAPTCHA.');
  }
}

  private resetForm(): void {
    this.userForm.reset();
    this.captchaToken = null;
    this.resetRecaptcha();
    
    // Ocultar mensaje de éxito después de 5 segundos
    setTimeout(() => {
      this.showSuccessMessage = false;
    }, 5000);
  }

  private resetRecaptcha(): void {
    if (this.recaptchaComponent) {
      this.recaptchaComponent.reset();
    }
    this.captchaToken = null;
  }

  private markFormGroupTouched(): void {
    Object.keys(this.userForm.controls).forEach(key => {
      this.userForm.get(key)?.markAsTouched();
    });
  }

  // Método para verificar si el botón debe estar deshabilitado
  isSubmitDisabled(): boolean {
    return this.userForm.invalid || !this.captchaToken || this.isSubmitting;
  }

  goBack(): void {
   this.router.navigate(['']);
  }
}