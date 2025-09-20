import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-recover-password',
  standalone: false,
  templateUrl: './recover-password.html',
  styleUrl: './recover-password.css'
})
export class RecoverPassword implements OnInit {
  userForm!: FormGroup;
  currentSize: 'small' | 'normal' | 'large' = 'normal';
  showPassword = false;
  showSuccessMessage = false;
  showTermsModal = false;
  formSubmitted = false;
  isLoading = false;
  errorMsg = '';
  showRecoverPassword = false;
  username!: string;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {
    const navigation = this.router.getCurrentNavigation();
    this.username = navigation?.extras.state?.['username'] || '';
    // Construcción del formulario
    this.buildForm();
  }
  private buildForm(): void {
    this.userForm = this.fb.group(
      {
        Password1: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).+$/)
            // minúscula, mayúscula, número y caracter especial
          ],
        ],
        Password2: ['', [Validators.required]],
      },
      { validators: this.passwordsMatchValidator } // validador cruzado
    );
  }
  ngOnInit(): void {

    if (!this.username) { this.router.navigate(['']); return; }
    console.log('Username recibido:', this.username);

  }

  passwordsMatchValidator: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    const pass1 = group.get('Password1')?.value;
    const pass2 = group.get('Password2')?.value;
    if (pass1 && pass2 && pass1 !== pass2) {
      group.get('Password2')?.setErrors({ mismatch: true });
      return { mismatch: true };
    }
    return null;
  };

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
    if (this.userForm.invalid) return;

    const formData = this.userForm.value;
    if (!this.username) {
      this.errorMsg = 'No se encontró el nombre de usuario en el sistema';
      return;
    }

    this.isLoading = true;
    const requestData = { username: this.username, password: formData.Password1 };
    this.auth.updatePassword(requestData).subscribe({
      next: () => {
        this.showSuccessMessage = true;
         this.alertService.createAlert('✅ Contraseña actualizada con exito', 'success', false).then(() => {
          this.router.navigate(['/']);
        });
        this.userForm.reset();
        this.isLoading = false;
        setTimeout(() => this.showSuccessMessage = false, 5000);
        this.router.navigate(['']);
      },
      error: (err) => {
        this.errorMsg = err?.error?.message || 'Error inesperado';
        this.isLoading = false;
      },
    });
  }

  // (Opcional) si usas modal de términos en el HTML
  openTermsModal() { this.showTermsModal = true; }
  closeTermsModal() { this.showTermsModal = false; }
  onReset(): void {
    this.userForm.reset();
    this.formSubmitted = false;
    this.errorMsg = '';
    this.showSuccessMessage = false;
  }
  // Variables para controlar el estado de los ojitos
  showPassword1 = false;
  showPassword2 = false;

  togglePassword1(): void {
    this.showPassword1 = !this.showPassword1;
  }

  togglePassword2(): void {
    this.showPassword2 = !this.showPassword2;
  }

  goBack(): void {
    this.router.navigate(['']);
  }

}
