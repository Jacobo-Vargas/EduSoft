import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { ContentService, ContentResponseDto } from '../../services/content.service';

@Component({
  selector: 'app-content',
  standalone: false,
  templateUrl: './content.html',
  styleUrls: ['./content.css']
})
export class ContentComponent implements OnInit, OnDestroy {

  contents: ContentResponseDto[] = [];
  loading = true;
  error: string | null = null;
  lessonId!: number;
  private subscriptions: Subscription[] = [];

  constructor(
    private contentService: ContentService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.lessonId = Number(this.route.snapshot.paramMap.get('lessonId'));
    this.loadContents();
  }

  private loadContents(): void {
    const sub = this.contentService.getContentsByLesson(this.lessonId).subscribe({
      next: (data) => {
        this.contents = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los contenidos';
        this.loading = false;
      }
    });
    this.subscriptions.push(sub);
  }

  deleteContent(content: ContentResponseDto): void {
    const confirmed = confirm(`¿Deseas eliminar el documento "${content.title}"?`);
    if (!confirmed) return;

    this.contentService.deleteContent(content.id).subscribe({
      next: () => {
        this.contents = this.contents.filter(c => c.id !== content.id);
      },
      error: () => {
        alert('No se pudo eliminar el documento');
      }
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
