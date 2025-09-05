import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LessonService, LessonResponseDto } from '../../services/lesson.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-lesson',
  standalone: false,
  templateUrl: './lesson.html',
  styleUrls: ['./lesson.css']
})
export class LessonComponent implements OnInit, OnDestroy {
  lessons: LessonResponseDto[] = [];
  loading = true;
  error: string | null = null;
  moduleId!: number;

  private subscriptions: Subscription[] = [];

  constructor(
    private lessonService: LessonService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // 📌 obtener el id del módulo de la URL
    this.moduleId = Number(this.route.snapshot.paramMap.get('moduleId'));
    console.log('📌 moduleId obtenido de la URL:', this.moduleId);

    this.loadLessons();
  }

  private loadLessons(): void {
    console.log(`📡 Solicitando lecciones para moduleId=${this.moduleId}`);

    const sub = this.lessonService.getLessonsByModule(this.moduleId).subscribe({
      next: (data) => {
        console.log('✅ Lecciones recibidas:', data);
        this.lessons = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar las lecciones';
        this.loading = false;
        console.error('❌ Error cargando lecciones:', err);
      }
    });

    this.subscriptions.push(sub);
  }

  ngOnDestroy(): void {
    console.log('🧹 Liberando suscripciones en LessonComponent');
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
