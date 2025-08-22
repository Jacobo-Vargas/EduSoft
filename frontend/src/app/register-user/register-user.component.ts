import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../usuario.service';

@Component({
  selector: 'app-register-user',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
    <div class="page-container">
      <!-- Logo arriba a la derecha -->
      <img src="assets/logo-uq.png" alt="Logo Universidad del Quindío" class="logo-uq" />

      <!-- Controles de accesibilidad (centrados vertical a la izquierda) -->
      <div class="accessibility-controls">
        <button type="button" (click)="setFontSize('normal')" data-tooltip="Disminuir tamaño">A-</button>
        <button type="button" (click)="setFontSize('large')" data-tooltip="Aumentar tamaño">A+</button>
      </div>

      <!-- Contenedor del formulario -->
      <div class="form-container" [ngClass]="currentSize">
        <h2>Registro de Usuario</h2>

        <!-- Mensaje de éxito -->
        <div *ngIf="showSuccessMessage" class="success-message">
          ✅ Usuario registrado con éxito
        </div>

        <form [formGroup]="userForm" (ngSubmit)="onSubmit()">

          <label>Número de documento*</label>
          <input type="text" formControlName="documentNumber" placeholder="Ingrese su número de documento" />
          <div *ngIf="userForm.get('documentNumber')?.invalid && userForm.get('documentNumber')?.touched" class="error-message">
            El número de documento es obligatorio
          </div>

          <label>Nombre*</label>
          <input type="text" formControlName="name" placeholder="Ingrese su nombre" />
          <div *ngIf="userForm.get('name')?.invalid && userForm.get('name')?.touched" class="error-message">
            El nombre es obligatorio
          </div>

          <label>Teléfono*</label>
          <input type="text" formControlName="phone" placeholder="Ingrese su teléfono" />
          <div *ngIf="userForm.get('phone')?.invalid && userForm.get('phone')?.touched" class="error-message">
            El teléfono es obligatorio
          </div>

          <label>Dirección*</label>
          <input type="text" formControlName="address" placeholder="Ingrese su dirección" />
          <div *ngIf="userForm.get('address')?.invalid && userForm.get('address')?.touched" class="error-message">
            La dirección es obligatoria
          </div>

          <label>Correo electrónico*</label>
          <input type="email" formControlName="email" placeholder="Ingrese su correo electrónico" />
          <div *ngIf="userForm.get('email')?.invalid && userForm.get('email')?.touched" class="error-message">
            <span *ngIf="userForm.get('email')?.errors?.['required']">El correo electrónico es obligatorio</span>
            <span *ngIf="userForm.get('email')?.errors?.['email']">El formato del correo electrónico no es válido</span>
          </div>

          <!-- Contenedor del label de contraseña -->
          <div class="password-label-container">
            <label>Contraseña*</label>
          </div>

          <!-- Input con ojo -->
          <div class="password-container">
            <input 
              [type]="showPassword ? 'text' : 'password'" 
              formControlName="password" 
              placeholder="Ingrese su contraseña" 
            />
            <span class="eye-icon" (click)="togglePassword()">
              {{ showPassword ? '🙈' : '👁️' }}
            </span>
          </div>
          <div *ngIf="userForm.get('password')?.invalid && userForm.get('password')?.touched" class="error-message">
            <span *ngIf="userForm.get('password')?.errors?.['required']">La contraseña es obligatoria</span>
            <span *ngIf="userForm.get('password')?.errors?.['minlength']">La contraseña debe tener al menos 8 caracteres</span>
            <span *ngIf="userForm.get('password')?.errors?.['passwordStrength']">La contraseña debe tener mayúscula, minúscula, número y carácter especial</span>
          </div>

          <button type="submit" [disabled]="userForm.invalid">Aceptar</button>
        </form>
      </div>
    </div>
  `,
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent {
  userForm: FormGroup;
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
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]]
    });
  }

  // Validador personalizado para fuerza de contraseña
  passwordStrengthValidator(control: any) {
    const value = control.value;
    if (!value) {
      return null;
    }

    const hasUppercase = /[A-Z]/.test(value);
    const hasLowercase = /[a-z]/.test(value);
    const hasNumber = /\d/.test(value);
    const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(value);

    const valid = hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
    
    if (!valid) {
      return { passwordStrength: true };
    }

    return null;
  }

  setFontSize(size: 'small' | 'normal' | 'large') {
    this.currentSize = size;
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (this.userForm.valid) {
      this.usuarioService.createUser(this.userForm.value).subscribe({
        next: (res) => {
          console.log('Usuario registrado:', res);
          this.showSuccessMessage = true;
          this.userForm.reset();
          
          // Ocultar mensaje después de 5 segundos
          setTimeout(() => {
            this.showSuccessMessage = false;
          }, 5000);
        },
        error: (err) => {
          console.error('Error al registrar usuario:', err);
          alert('❌ Error al registrar usuario');
        }
      });
    } else {
      // Marcar todos los campos como tocados para mostrar errores
      Object.keys(this.userForm.controls).forEach(key => {
        this.userForm.get(key)?.markAsTouched();
      });
    }
  }
}
