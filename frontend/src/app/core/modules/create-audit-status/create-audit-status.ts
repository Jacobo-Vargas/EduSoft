import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../services/course-service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-create-audit-status',
  standalone: false,
  templateUrl: './create-audit-status.html',
  styleUrls: ['./create-audit-status.css'] 
})

export class CreateAuditStatus {
  auditStatusForm!: FormGroup;
  showSuccessMessage = false;
  formSubmitted = false;
  errorMsg = '';
  isLoading = false;

  constructor(private fb: FormBuilder, private statusAudit: CourseService) {}

  ngOnInit(): void {
    this.auditStatusForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
    });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.auditStatusForm.controls;
  }

  onReset(): void {
    this.auditStatusForm.reset();
    this.formSubmitted = false;
    this.errorMsg = '';
    this.showSuccessMessage = false;
  }

  onSubmit(): void {
   console.log("Submit disparado", this.auditStatusForm.value);
    this.formSubmitted = true;
    this.errorMsg = '';

    if (this.auditStatusForm.invalid) {
      Object.values(this.auditStatusForm.controls).forEach(c => c.markAsTouched());
      return;
    }

    // payload limpio (trim)
    const payload = {
      name: (this.f['name'].value as string).trim(),
      description: (this.f['description'].value as string).trim(),
    };

    if (!payload.name || !payload.description) {
      this.errorMsg = 'Por favor completa los campos requeridos.';
      return;
    }

    this.isLoading = true;
    this.statusAudit.createStatusAudi(payload)
      .pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: () => {
          this.showSuccessMessage = true;
          this.auditStatusForm.reset();
          this.formSubmitted = false;
          setTimeout(() => this.showSuccessMessage = false, 4000);
        },
        error: (err) => {
          const status = err?.status;
          const msg = err?.error?.message || 'Error inesperado';
          if (status === 401) this.errorMsg = 'Credenciales inválidas';
          else if (status === 403) this.errorMsg = 'Cuenta no verificada';
          else if (status === 503) this.errorMsg = 'Servicio no disponible. Inténtalo más tarde.';
          else this.errorMsg = msg;
        }
      });
  }
}

