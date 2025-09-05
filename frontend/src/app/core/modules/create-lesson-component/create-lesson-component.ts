import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LessonService } from '../../services/lesson.service';

@Component({
  selector: 'app-create-lesson-component',
  templateUrl: './create-lesson-component.html',
  styleUrls: ['./create-lesson-component.css'],
  standalone: false
})
export class CreateLessonComponent implements OnInit {
  lessonForm!: FormGroup;
  moduleId!: number;

  constructor(
    private fb: FormBuilder,
    private lessonService: LessonService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
  this.moduleId = Number(this.route.snapshot.paramMap.get('moduleId'));
  console.log("🔍 moduleId capturado desde ruta:", this.moduleId);

  if (!this.moduleId || isNaN(this.moduleId)) {
    console.error("⚠️ No se encontró moduleId en la URL. Redirigiendo...");
    this.router.navigate(['/modules']);
    return;
  }

  this.lessonForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(5)]], 
    description: ['', [Validators.required, Validators.minLength(10)]], 
    displayOrder: [1, [Validators.required, Validators.min(1)]]
  });
}


  get f() {
    return this.lessonForm.controls;
  }

  onSubmit(): void {
    if (this.lessonForm.invalid) {
      Object.values(this.lessonForm.controls).forEach(c => c.markAsTouched());
      console.warn("⚠️ Formulario inválido:", this.lessonForm.value);
      return;
    }

    const lessonData = {
      name: this.lessonForm.value.name,
      description: this.lessonForm.value.description,
      moduleId: this.moduleId,
      displayOrder: this.lessonForm.value.displayOrder
    };

    console.log("📤 Datos finales al backend:", JSON.stringify(lessonData, null, 2));

    this.lessonService.createLesson(lessonData).subscribe({
      next: (res) => {
        console.log("✅ Lección creada con éxito:", res);
        this.goBack();
      },
      error: (err) => {
        console.error("❌ Error al crear la lección:", err);
        console.error("📌 Detalle del error:", err.error);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/modules', this.moduleId, 'lessons']);
  }
}
