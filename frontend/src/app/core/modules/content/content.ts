import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ContentService, ContentResponseDto } from '../../services/content.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-content',
  standalone: false,
  templateUrl: './content.html',
  styleUrls: ['./content.css']
})
export class ContentComponent implements OnInit, OnDestroy {

  contents: ContentResponseDto[] = [];
  loading = false;
  error: string | null = null;
  moduleId!: number;
  lessonId!: number;
  private subscriptions: Subscription[] = [];

  constructor(
    private contentService: ContentService,
    private route: ActivatedRoute,
    private router: Router,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.moduleId = Number(this.route.snapshot.paramMap.get('moduleId'));
    this.lessonId = Number(this.route.snapshot.paramMap.get('lessonId'));
    this.loadContents();
  }

  private loadContents(): void {
    this.loading = true;
    const sub = this.contentService.getContentsByLesson(this.lessonId).subscribe({
      next: (data) => {
        this.contents = data;
        this.loading = false;
      },
      error: (err) => {
        this.handleError('Error al cargar los contenidos', err);
        this.loading = false;
      }
    });
    this.subscriptions.push(sub);
  }

  deleteContent(content: ContentResponseDto): void {
    this.alertService.confirmCustomAlert(
      `¿Deseas eliminar el documento "${content.title}"?`,
      'question',
      this.alertService.jsonData['alert']?.['btnAccept'] || 'Aceptar',
      this.alertService.jsonData['alert']?.['btnCancel'] || 'Cancelar'
    ).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;
      const sub = this.contentService.deleteContent(content.id).subscribe({
        next: () => {
          this.contents = this.contents.filter(c => c.id !== content.id);
          this.alertService.createAlert('Documento eliminado correctamente', 'success', false);
          this.loading = false;
        },
        error: (err) => {
          this.handleError('No se pudo eliminar el documento', err);
          this.loading = false;
        }
      });
      this.subscriptions.push(sub);
    });
  }

  private handleError(msg: string, err: any): void {
    const errorMessage = err?.error?.message || msg;
    this.error = errorMessage;
    this.alertService.createAlert(errorMessage, 'error', false);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  goBack(): void {
    this.router.navigate(['/modules', this.moduleId, 'lessons',]);
  }
}