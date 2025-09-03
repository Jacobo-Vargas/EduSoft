import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { CourseService } from '../../services/course-service';

@Component({
  selector: 'app-create-categories',
  standalone: false,
  templateUrl: './create-categories.html',
  styleUrl: './create-categories.css'
})
export class CreateCategories implements OnInit {
  categorieForm!: FormGroup;
  showSuccessMessage = false;
  formSubmitted = false;
  errorMsg = '';
  isLoading = false;

  constructor(private fb: FormBuilder, private categories: CourseService) {}

  ngOnInit(): void {
    this.categorieForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
    });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.categorieForm.controls;
  }

  onReset(): void {
    this.categorieForm.reset();
    this.formSubmitted = false;
    this.errorMsg = '';
    this.showSuccessMessage = false;
  }

  onSubmit(): void {
    this.formSubmitted = true;
    this.errorMsg = '';

    if (this.categorieForm.invalid) {
      Object.values(this.categorieForm.controls).forEach(c => c.markAsTouched());
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
    this.categories.createCategorie(payload)
      .pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: () => {
          this.showSuccessMessage = true;
          this.categorieForm.reset();
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
