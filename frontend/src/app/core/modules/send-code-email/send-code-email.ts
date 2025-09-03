import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-send-email',
  templateUrl: './send-code-email.html',
  styleUrl: './send-code-email.css',
  standalone: false,
})
export class SendEmail {
  fontSize = 16; // tamaño base en px
 userForm!: FormGroup;
  isLoading = false;
  errorMsg = '';
  isCodeSent = false;
  formSubmitted = false;
  showSuccessMessage = false;
  generatedCode = '';
  savedUsername = ''; 

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    // Primera fase: solo username
    this.userForm = this.fb.group({
      username: ['', [Validators.required]]
    });
  }

  get f() { return this.userForm.controls; }

  onSubmit(): void {
    this.formSubmitted = true;
    this.errorMsg = '';

    if (this.userForm.invalid) {
      Object.values(this.userForm.controls).forEach(c => c.markAsTouched());
      return;
    }

    this.isLoading = true;

    // Si ya se envió el código → verificar
    if (this.isCodeSent) {
      const code =
        this.userForm.value.code1 +
        this.userForm.value.code2 +
        this.userForm.value.code3 +
        this.userForm.value.code4;

      console.log('Verificando código:', code);

      this.auth.verifyCode(code, { username: this.savedUsername }).subscribe({
        next: () => {
          this.showSuccessMessage = true;
          this.isLoading = false;
          this.userForm.reset();
          this.router.navigate(['/recover-password'], { state: { username: this.savedUsername } });
          setTimeout(() => this.showSuccessMessage = false, 5000);
        },
        error: (err) => {
          this.errorMsg = err?.error?.message || 'El código ingresado es incorrecto';
          this.isLoading = false;
          console.error('[HTTP ERROR]', err);
        }
      });

    } else {
      // Guardamos username antes de limpiar
      this.savedUsername = this.userForm.value.username;

      this.auth.startReset({ username: this.savedUsername }).subscribe({
        next: (response) => {
          this.showSuccessMessage = true;
          this.isCodeSent = true;
          this.generatedCode = response;
          this.isLoading = false;

          this.userForm = this.fb.group({
            code1: ['', [Validators.required, Validators.pattern('[0-9]')]],
            code2: ['', [Validators.required, Validators.pattern('[0-9]')]],
            code3: ['', [Validators.required, Validators.pattern('[0-9]')]],
            code4: ['', [Validators.required, Validators.pattern('[0-9]')]],
          });

          setTimeout(() => this.showSuccessMessage = false, 5000);
        },
        error: (err) => {
          this.errorMsg = err?.error?.message || 'Error inesperado';
          this.isLoading = false;
          console.error('[HTTP ERROR]', err);
        }
      });
    }
  }

  autoFocusNext(event: Event, index: number) {
    const input = event.target as HTMLInputElement;
    if (input.value && index < 4) {
      const nextInput = input.parentElement?.children[index] as HTMLInputElement;
      nextInput?.focus();
    }
  }

  onReset(): void {
    this.userForm.reset();
    this.formSubmitted = false;
    this.errorMsg = '';
    this.showSuccessMessage = false;
  }
  goBack(): void {
   this.router.navigate(['']);
}

}