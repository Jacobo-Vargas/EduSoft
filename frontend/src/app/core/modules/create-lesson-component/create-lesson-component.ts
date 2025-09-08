import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LessonService } from '../../services/lesson.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-create-lesson-component',
  templateUrl: './create-lesson-component.html',
  styleUrls: ['./create-lesson-component.css'],
  standalone: false
})
export class CreateLessonComponent implements OnInit {
  lessonForm!: FormGroup;
  moduleId!: number;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private lessonService: LessonService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.moduleId = Number(this.route.snapshot.paramMap.get('moduleId'));
    console.log("🔍 moduleId capturado desde ruta:", this.moduleId);

    if (!this.moduleId || isNaN(this.moduleId)) {
      this.alertService.createAlert('⚠️ No se encontró moduleId en la URL', 'warning', false).then(() => {
        this.router.navigate(['/modules']);
      });
      return;
    }

    this.lessonForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
    });
  }

  get f() {
    return this.lessonForm.controls;
  }

  onSubmit(): void {
    if (this.lessonForm.invalid) {
      Object.values(this.lessonForm.controls).forEach(c => c.markAsTouched());
      this.alertService.createAlert('⚠️ Formulario inválido, verifica los campos', 'warning', false);
      return;
    }

    const lessonData = {
      name: this.lessonForm.value.name,
      description: this.lessonForm.value.description,
      moduleId: this.moduleId,
    };

    this.loading = true;

    this.lessonService.createLesson(lessonData).subscribe({
      next: (res) => {
        this.loading = false;
        this.alertService.createAlert('✅ Lección creada con éxito', 'success', false).then(() => {
          this.goBack();
        });
      },
      error: (err) => {
        this.loading = false;
        const errorMsg = err?.error?.message || 'Error al crear la lección';
        this.alertService.createAlert(`❌ ${errorMsg}`, 'error', false);
        console.error("❌ Error al crear la lección:", err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/modules', this.moduleId, 'lessons']);
  }
}
