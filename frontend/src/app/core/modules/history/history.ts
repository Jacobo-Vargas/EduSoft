import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HistoryService, CourseEventResponseDto } from '../../services/history.service';

@Component({
  selector: 'app-history',
  standalone: false,
  templateUrl: './history.html',
  styleUrls: ['./history.css']
})
export class History implements OnInit {
  history: CourseEventResponseDto[] = [];
  loading = true;
  error: string | null = null;
  courseId!: number;

  
  constructor(private route: ActivatedRoute, private historyService: HistoryService) { }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));
    this.loadHistory();
  }

  private loadHistory(): void {
    this.historyService.getCourseHistory(this.courseId).subscribe({
      next: (data) => {
        this.history = [...this.history, ...data];
        this.loading = false;
      }
      ,
      error: (err) => {
        console.error('❌ Error cargando historial:', err);
        this.error = 'No se pudo cargar el historial';
        this.loading = false;
      }
    });
  }
}