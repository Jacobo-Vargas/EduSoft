import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ContentService } from '../../services/content.service';
import { AlertService } from '../../services/alert.service';
import imageCompression from 'browser-image-compression';

@Component({
  selector: 'app-create-content-component',
  templateUrl: './create-content-component.html',
  styleUrls: ['./create-content-component.css'],
  standalone: false
})
export class CreateContentComponent implements OnInit {
  contentForm!: FormGroup;
  courseId!: number;
  moduleId!: number;
  lessonId!: number;
  selectedFile: File | null = null;
  fileError = false;
  contents: any[] = [];
  loading = false;

  constructor(
    private fb: FormBuilder,
    private contentService: ContentService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));
    this.moduleId = Number(this.route.snapshot.paramMap.get('moduleId'));
    this.lessonId = Number(this.route.snapshot.paramMap.get('lessonId'));

    if (!this.lessonId || isNaN(this.lessonId)) {
      this.alertService.createAlert('⚠️ No se encontró lessonId en la URL', 'warning', false)
        .then(() => this.router.navigate(['/modules']));
      return;
    }

    this.contentForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(10)]]
    });

    this.loadContents();
  }

  get f() {
    return this.contentForm.controls;
  }

  loadContents(): void {
    this.contentService.getContentsByLesson(this.lessonId).subscribe({
      next: (res) => this.contents = res,
      error: (err) => {
        const msg = err?.error?.message || 'No se pudo cargar los contenidos';
        this.alertService.createAlert(msg, 'error', false);
        console.error('❌ Error al cargar contenidos:', err);
      }
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) {
      this.selectedFile = null;
      this.fileError = true;
      return;
    }

    if (file.size > 20 * 1024 * 1024) {
      this.alertService.createAlert('⚠️ El archivo es demasiado grande. Máximo 20 MB', 'warning', false);
      this.selectedFile = null;
      this.fileError = true;
      return;
    }

    if (file.type.startsWith("image/")) {
      const options = { maxSizeMB: 1, maxWidthOrHeight: 1920, useWebWorker: true };
      imageCompression(file, options)
        .then(compressedFile => {
          this.selectedFile = compressedFile;
          this.fileError = false;
        })
        .catch(error => {
          this.selectedFile = null;
          this.fileError = true;
          this.alertService.createAlert('❌ Error al comprimir la imagen', 'error', false);
          console.error('❌ Error al comprimir la imagen:', error);
        });
    } else {
      this.selectedFile = file;
      this.fileError = false;
    }
  }

  onSubmit(): void {
    if (this.contentForm.invalid) {
      Object.values(this.contentForm.controls).forEach(c => c.markAsTouched());
      this.alertService.createAlert('⚠️ Formulario inválido', 'warning', false);
      return;
    }

    const dto = {
      title: this.contentForm.value.title,
      description: this.contentForm.value.description,
      lessonId: this.lessonId
    };

    this.loading = true;

    this.contentService.createContent(dto, this.selectedFile || undefined)
      .subscribe({
        next: () => {
          this.loading = false;
          this.alertService.createAlert('✅ Contenido creado con éxito', 'success', false).then(() => {
            this.router.navigate([
              '/modules',
              this.courseId,
              'lessons',
              this.moduleId,
              'contents',
              this.lessonId
            ]);
          });
        },

        error: (err) => {
          this.loading = false;
          const msg = err?.error?.message || 'Error al crear el contenido';
          this.alertService.createAlert(`❌ ${msg}`, 'error', false);
          console.error('❌ Error al crear contenido:', err);
        }
      });
  }

  goBack(): void {
    this.router.navigate(['/modules', this.courseId, 'lessons', this.moduleId, 'contents', this.lessonId]);
  }
}
