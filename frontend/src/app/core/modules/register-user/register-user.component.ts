import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';

declare var grecaptcha: any;

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

          <!-- Términos y Condiciones -->
          <div class="terms-container">
            <label class="checkbox-container">
              <input type="checkbox" formControlName="acceptTerms" />
              <span class="checkmark"></span>
              He leído y acepto los 
              <button type="button" class="link-button" (click)="openTermsModal()">
                Términos y Condiciones
              </button>
              <span class="required-asterisk">*</span>
            </label>
            <div *ngIf="userForm.get('acceptTerms')?.invalid && userForm.get('acceptTerms')?.touched" class="error-message">
              Debe aceptar los términos y condiciones
            </div>
          </div>

          <!-- reCAPTCHA -->
          <div class="recaptcha-container">
            <div id="recaptcha" class="g-recaptcha" 
                 data-sitekey="6LfguLArAAAAAEOLkYADBHDG1tYg4XnqucxJ99m4"
                 data-callback="onCaptchaSuccess"
                 data-expired-callback="onCaptchaExpired">
            </div>
            <div *ngIf="!captchaValid && formSubmitted" class="error-message">
              Por favor, complete el reCAPTCHA
            </div>
          </div>

          <button type="submit" [disabled]="userForm.invalid || !captchaValid">Aceptar</button>
        </form>
      </div>

      <!-- Modal de Términos y Condiciones -->
      <div *ngIf="showTermsModal" class="modal-overlay" (click)="closeTermsModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Términos y Condiciones</h3>
            <button type="button" class="close-button" (click)="closeTermsModal()">×</button>
          </div>
          <div class="modal-body">
            <div class="terms-text">
              <h4>1. Objeto</h4>
          <p>
            El presente documento establece los Términos y Condiciones de uso de la plataforma EduSoft, 
            desarrollada como parte del proyecto académico de Ingeniería de Software III. EduSoft tiene como 
            finalidad apoyar los procesos de gestión académica y administrativa en entornos universitarios, 
            brindando a los usuarios herramientas para la interacción, el registro y la consulta de información 
            de manera eficiente.
          </p>

          <h4>2. Aceptación de los Términos</h4>
          <p>
            El acceso y uso de EduSoft por parte de estudiantes, docentes, administradores y demás usuarios 
            implica la aceptación plena y sin reservas de estos Términos y Condiciones. En caso de no estar 
            de acuerdo, el usuario deberá abstenerse de utilizar la plataforma.
          </p>

          <h4>3. Usuarios y Roles</h4>
          <ul>
            <li><strong>Estudiantes:</strong> Acceden a información académica, realizan consultas y gestiones relacionadas con su proceso educativo.</li>
            <li><strong>Docentes:</strong> Registran información académica, gestionan cursos y realizan seguimiento a los estudiantes.</li>
            <li><strong>Administradores:</strong> Configuran y supervisan el funcionamiento de la plataforma, asegurando la calidad del servicio.</li>
          </ul>

          <h4>4. Responsabilidades de los Usuarios</h4>
          <ul>
            <li>Utilizar la plataforma únicamente para los fines establecidos en el proyecto.</li>
            <li>No manipular, alterar ni intentar acceder sin autorización a información restringida.</li>
            <li>Ser responsables de la veracidad de los datos registrados en el sistema.</li>
          </ul>

          <h4>5. Responsabilidades del Proyecto EduSoft</h4>
          <ul>
            <li>Garantizar la disponibilidad y correcto funcionamiento de la plataforma en los entornos definidos.</li>
            <li>Asegurar la integridad de los datos y la confidencialidad de la información.</li>
            <li>Implementar prácticas de calidad, de acuerdo con el Plan de Gestión de la Calidad del Proyecto.</li>
          </ul>

          <h4>6. Propiedad Intelectual</h4>
          <p>
            EduSoft es un producto desarrollado en el marco académico, por lo tanto, su uso está limitado a fines 
            educativos y de investigación. Queda prohibida su comercialización sin la debida autorización de los 
            responsables del proyecto.
          </p>

          <h4>7. Privacidad y Protección de Datos</h4>
          <p>
            EduSoft recopilará y almacenará información académica de los usuarios con el único fin de cumplir 
            los objetivos del proyecto. Los datos no serán compartidos con terceros sin autorización, salvo 
            requerimiento legal.
          </p>

          <h4>8. Limitación de Responsabilidad</h4>
          <p>
            EduSoft se encuentra en etapa de desarrollo académico. Por tanto, no garantiza una disponibilidad 
            absoluta ni se responsabiliza por fallos técnicos o pérdidas de datos derivados del uso de la plataforma.
          </p>

          <h4>9. Modificaciones de los Términos</h4>
          <p>
            El equipo de desarrollo de EduSoft se reserva el derecho de modificar los presentes Términos y 
            Condiciones cuando sea necesario, comunicando los cambios a los usuarios a través de la plataforma.
          </p>

          <h4>10. Ley Aplicable y Jurisdicción</h4>
          <p>
            Estos Términos y Condiciones se rigen por las leyes de la República de Colombia. Cualquier controversia 
            que se derive de su interpretación o aplicación será resuelta por los mecanismos de conciliación y 
            arbitraje establecidos en la normativa vigente.
          </p>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn-secondary" (click)="closeTermsModal()">Cerrar</button>
            <button type="button" class="btn-primary" (click)="acceptTermsFromModal()">Aceptar términos</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {
  userForm: FormGroup;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword: boolean = false;
  showSuccessMessage: boolean = false;
  showTermsModal: boolean = false;
  captchaValid: boolean = false;
  formSubmitted: boolean = false;

  constructor(private fb: FormBuilder, private usuarioService: UsuarioService) {
    this.userForm = this.fb.group({
      documentNumber: ['', Validators.required],
      name: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
      acceptTerms: [false, Validators.requiredTrue]
    });

    // Configurar callbacks globales para reCAPTCHA
    (window as any).onCaptchaSuccess = (token: string) => {
      this.captchaValid = true;
      console.log('reCAPTCHA completado:', token);
    };

    (window as any).onCaptchaExpired = () => {
      this.captchaValid = false;
      console.log('reCAPTCHA expirado');
    };
  }

  ngOnInit() {
    this.loadRecaptcha();
  }

  loadRecaptcha() {
    // Cargar el script de reCAPTCHA si no está ya cargado
    if (!document.querySelector('script[src*="recaptcha"]')) {
      const script = document.createElement('script');
      script.src = 'https://www.google.com/recaptcha/api.js';
      script.async = true;
      script.defer = true;
      document.head.appendChild(script);
    }
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

  openTermsModal() {
    this.showTermsModal = true;
    document.body.style.overflow = 'hidden'; // Evitar scroll del fondo
  }

  closeTermsModal() {
    this.showTermsModal = false;
    document.body.style.overflow = 'auto';
  }

  acceptTermsFromModal() {
    this.userForm.patchValue({ acceptTerms: true });
    this.closeTermsModal();
  }

  onSubmit() {
    this.formSubmitted = true;

    if (this.userForm.valid && this.captchaValid) {
      const formData = {
        ...this.userForm.value,
        recaptchaToken: grecaptcha.getResponse()
      };

      this.usuarioService.createUser(formData).subscribe({
        next: (res) => {
          console.log('Usuario registrado:', res);
          this.showSuccessMessage = true;
          this.userForm.reset();
          this.captchaValid = false;
          this.formSubmitted = false;
          
          // Reset reCAPTCHA
          if (typeof grecaptcha !== 'undefined') {
            grecaptcha.reset();
          }
          
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