import { Component, OnInit } from '@angular/core';
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
  userForm: FormGroup;
  captchaToken: string | null = null;   // 🔹 aquí guardamos el token del reCAPTCHA
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword: boolean = false;
  showSuccessMessage: boolean = false;

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

  // 🔹 Método que recibe el token desde el componente reCAPTCHA
  onToken(token: string) {
    this.captchaToken = token;
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
    if (!this.captchaToken) {   // 🔹 validar primero el reCAPTCHA
      alert('Por favor valida el reCAPTCHA');
      return;
    }

    if (this.userForm.valid) {
      const formData = {
        ...this.userForm.value,
        captcha: this.captchaToken   // 🔹 incluimos el token
      };

      this.usuarioService.createUser(formData).subscribe({
        next: (response) => {
          console.log('Usuario registrado con éxito:', response);
          this.showSuccessMessage = true;
          this.userForm.reset();
          this.captchaToken = null;
        },
        error: (error) => {
          console.error('Error al registrar usuario:', error);
        }
      });
    }
  }
}
