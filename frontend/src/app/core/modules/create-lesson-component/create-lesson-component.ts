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

    this.lessonForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      displayOrder: [1, [Validators.required, Validators.min(1)]],
      videoUrl: ['']
    });
  }

  get f() {
    return this.lessonForm.controls;
  }

  onSubmit(): void {
    if (this.lessonForm.invalid) {
      Object.values(this.lessonForm.controls).forEach(c => c.markAsTouched());
      return;
    }

    const lessonData = {
      ...this.lessonForm.value,
      moduleId: this.moduleId
    };

    this.lessonService.createLesson(lessonData).subscribe({
      next: () => {
        this.router.navigate(['/modules', this.moduleId, 'lessons']);
      },
      error: (err) => {
        console.error('❌ Error al crear la lección:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/modules', this.moduleId, 'lessons']);
  }
}
