import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';
import { RecaptchaComponent } from '../recaptcha/recaptcha.component';

@Component({
  selector: 'app-register-user',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RecaptchaComponent],
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

  constructor(private fb: FormBuilder, private usuarioService: UsuarioService) {
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

  onSubmit(): void {

    // Validaciones
    if (this.isSubmitting) {
      console.warn('Ya se está procesando un envío');
      return;
    }

    if (!this.captchaToken) {
      alert('Por favor completa la verificación reCAPTCHA');
      return;
    }

    if (!this.userForm.valid) {
      console.warn('Formulario inválido');
      this.markFormGroupTouched();
      return;
    }

    // Proceder con el envío
    this.isSubmitting = true;
    const formData = {
      ...this.userForm.value,
      captcha: this.captchaToken
    };


    this.usuarioService.createUser(formData).subscribe({
      next: (response) => {
        console.log('✅ Usuario registrado con éxito:', response);
        this.showSuccessMessage = true;
        this.resetForm();
      },
      error: (error) => {
        console.error('❌ Error al registrar usuario:', error);
        
        // Mostrar mensaje de error más específico
        let errorMessage = 'Error al registrar usuario. ';
        if (error.status === 400) {
          errorMessage += 'Datos inválidos o reCAPTCHA inválido.';
        } else if (error.status === 500) {
          errorMessage += 'Error del servidor. Intenta más tarde.';
        } else {
          errorMessage += 'Problema de conexión.';
        }
        
        alert(errorMessage);
        this.resetRecaptcha();
      },
      complete: () => {
        this.isSubmitting = false;
      }
    });
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
}