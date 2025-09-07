import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModuleService } from '../../services/module.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-create-module',
  standalone: false,
  templateUrl: './create-module-component.html',
  styleUrls: ['./create-module-component.css']
})
export class CreateModuleComponent implements OnInit {
  moduleForm!: FormGroup;
  courseId!: number;
  loading = false;
  message: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private moduleService: ModuleService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    // obtener el courseId desde la URL
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));

    // inicializar formulario
    this.moduleForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(120)]],
      description: ['', [Validators.maxLength(800)]],
      displayOrder: ['', Validators.required],
      isVisible: [true]
    });
  }

  onSubmit(): void {
    if (this.moduleForm.invalid) {
      this.moduleForm.markAllAsTouched();
      return;
    }

    const payload = {
      ...this.moduleForm.value,
      courseId: this.courseId
    };

    this.loading = true;

    this.moduleService.createModule(payload).subscribe({
      next: () => {
        this.loading = false;
        this.alertService.createAlert(
          'Módulo creado con éxito',
          'success',
          false
        ).then(() => {
          this.router.navigate(['/modules', this.courseId]);
        });
      },
      error: (err) => {
        this.loading = false;
        const errorMsg = err?.error?.message || 'Error al crear el módulo';
        this.alertService.createAlert(errorMsg, 'error', false);
        console.error('❌ Error al crear módulo:', err);
      }
    });
  }
}
