import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CourseService } from '../../services/course-service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-create-status-course',
   imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './create-status-course.html',
  styleUrl: './create-status-course.css'
})
export class CreateStatusCourse {
  showSuccessMessage = false;
  formSubmitted = false;
  errorMsg = '';
  isLoading = false;
statusForm!: FormGroup; // Definimos el formulario


 constructor(private fb: FormBuilder, private statusCourses: CourseService) {}

  ngOnInit(): void {
    this.statusForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
    });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.statusForm.controls;
  }

  onReset(): void {
    this.statusForm.reset();
    this.formSubmitted = false;
    this.errorMsg = '';
    this.showSuccessMessage = false;
  }

  onSubmit(): void {
    this.formSubmitted = true;
    this.errorMsg = '';

    if (this.statusForm.invalid) {
      Object.values(this.statusForm.controls).forEach(c => c.markAsTouched());
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
    this.statusCourses.createStatusCourse(payload)
      .pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: () => {
          this.showSuccessMessage = true;
          this.statusForm.reset();
          this.formSubmitted = false;
          setTimeout(() => this.showSuccessMessage = false, 4000);
        },
        error: (err: { status: any; error: { message: string; }; }) => {
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
