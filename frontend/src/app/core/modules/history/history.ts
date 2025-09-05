import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HistoryService, CourseEventResponseDto } from '../../services/history.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-history',
  standalone: false,
  templateUrl: './history.html',
  styleUrls: ['./history.css']
})
export class History implements OnInit {
  history: CourseEventResponseDto[] = [];
  loading = false;
  error: string | null = null;
  courseId!: number;

  constructor(
    private route: ActivatedRoute,
    private historyService: HistoryService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));
    this.loadHistory();
  }

  private loadHistory(): void {
    this.loading = true;
    this.historyService.getCourseHistory(this.courseId).subscribe({
      next: (data) => {
        this.history = [...this.history, ...data];
        this.loading = false;
      },
      error: (err) => {
        const errorMsg = err?.error?.message || 'No se pudo cargar el historial';
        this.error = errorMsg;
        this.alertService.createAlert(errorMsg, 'error', false);
        this.loading = false;
        console.error('❌ Error cargando historial:', err);
      }
    });
  }
}
