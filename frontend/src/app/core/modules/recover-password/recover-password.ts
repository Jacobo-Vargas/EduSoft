import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RecoverPasswordService } from '../../services/recover-password.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recover-password',
  templateUrl: './recover-password.html',
  imports: [ReactiveFormsModule, CommonModule],
  styleUrls: ['./recover-password.css']
})
export class RecoverPassword {

  currentSize: 'small' | 'normal' | 'large' = 'normal';
  userForm!: FormGroup;
  errorMsg: string = '';
  successMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private recoverPasswordService: RecoverPasswordService
  ) {
    // Construcción del formulario
    this.userForm = this.fb.group({
      codigo1: ['', [Validators.required]],
      codigo2: ['', [Validators.required]],
      codigo3: ['', [Validators.required]],
      codigo4: ['', [Validators.required]],
    });
  }

  // Acceso rápido a los controles en el template
  get f() { return this.userForm.controls; }

  // Accesibilidad (A-/A+)
  setFontSize(size: 'small' | 'normal' | 'large') {
    this.currentSize = size;
  }

  // Método de envío
  onSubmit() {
    if (this.userForm.invalid) {
      this.errorMsg = 'Todos los campos son obligatorios';
      return;
    }

    // Asegurarse de que los valores sean strings antes de concatenarlos
    const code = `${String(this.userForm.value.codigo1)}${String(this.userForm.value.codigo2)}${String(this.userForm.value.codigo3)}${String(this.userForm.value.codigo4)}`;
  }
}
