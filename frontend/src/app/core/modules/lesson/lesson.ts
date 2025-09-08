import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LessonService, LessonResponseDto } from '../../services/lesson.service';
import { Subscription } from 'rxjs';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-lesson',
  standalone: false,
  templateUrl: './lesson.html',
  styleUrls: ['./lesson.css']
})
export class LessonComponent implements OnInit, OnDestroy {
  lessons: LessonResponseDto[] = [];
  loading = false;
  error: string | null = null;
  moduleId!: number;
  lessonId!: number;

  private subscriptions: Subscription[] = [];

  constructor(
    private lessonService: LessonService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.moduleId = Number(this.route.snapshot.paramMap.get('moduleId'));
    this.lessonId = Number(this.route.snapshot.paramMap.get('lessonId'));
    this.loadLessons();
  }

  private loadLessons(): void {
    this.loading = true;

    const sub = this.lessonService.getLessonsByModule(this.moduleId).subscribe({
      next: (data) => {
        this.lessons = data;
        this.loading = false;
      },
      error: (err) => {
        this.handleError('Error al cargar las lecciones', err);
        this.loading = false;
      }
    });

    this.subscriptions.push(sub);
  }

  deleteLesson(lesson: LessonResponseDto) {
    this.alertService.confirmCustomAlert(
      `¿Deseas eliminar la lección "${lesson.name}"?`,
      'question',
      this.alertService.jsonData['alert']?.['btnAccept'] || 'Aceptar',
      this.alertService.jsonData['alert']?.['btnCancel'] || 'Cancelar'
    ).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;
      const sub = this.lessonService.deleteLesson(lesson.id).subscribe({
        next: () => {
          this.lessons = this.lessons.filter(l => l.id !== lesson.id);
          this.alertService.createAlert(
            `Lección "${lesson.name}" eliminada correctamente`,
            'success',
            false
          );
          this.loading = false;
        },
        error: (err) => {
          this.handleError('No se pudo eliminar la lección', err);
          this.loading = false;
        }
      });

      this.subscriptions.push(sub);
    });
  }

  private handleError(msg: string, err: any) {
    const errorMessage = err?.error?.message || msg;
    this.error = errorMessage;
    this.alertService.createAlert(errorMessage, 'error', false);
    console.error('❌', errorMessage, err);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  goBack(): void {
    this.router.navigate(['/modules', this.moduleId]);
  }
}
