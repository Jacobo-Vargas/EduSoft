import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ContentService } from '../../services/content.service';
import imageCompression from 'browser-image-compression';

@Component({
  selector: 'app-create-content-component',
  templateUrl: './create-content-component.html',
  styleUrls: ['./create-content-component.css'],
  standalone: false
})
export class CreateContentComponent implements OnInit {
  contentForm!: FormGroup;
  lessonId!: number;
  selectedFile: File | null = null;
  fileError = false;
  contents: any[] = [];

  constructor(
    private fb: FormBuilder,
    private ContentService: ContentService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.lessonId = Number(this.route.snapshot.paramMap.get('lessonId'));
    console.log("🔍 lessonId capturado desde ruta:", this.lessonId);

    if (!this.lessonId || isNaN(this.lessonId)) {
      console.error("⚠️ No se encontró lessonId en la URL. Redirigiendo...");
      this.router.navigate(['/modules']);
      return;
    }

    this.contentForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      displayOrder: [1, [Validators.required, Validators.min(1)]]
    });

    this.loadContents();
  }

  get f() {
    return this.contentForm.controls;
  }

  // Cargar contenidos existentes de la lección
  loadContents(): void {
    this.ContentService.getContentsByLesson(this.lessonId).subscribe({
      next: (res) => this.contents = res,
      error: (err) => console.error('❌ Error al cargar contenidos:', err)
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) {
      this.selectedFile = null;
      this.fileError = true;
      return;
    }

    if (file.size > 20 * 1024 * 1024) { // 20 MB
      alert("⚠️ El archivo es demasiado grande. Máximo permitido: 20 MB");
      this.selectedFile = null;
      this.fileError = true;
      return;
    }

    if (file.type.startsWith("image/")) {
      const options = { maxSizeMB: 1, maxWidthOrHeight: 1920, useWebWorker: true };
      imageCompression(file, options)
        .then(compressedFile => {
          console.log("✅ Imagen comprimida:", compressedFile);
          this.selectedFile = compressedFile;
          this.fileError = false;
        })
        .catch(error => {
          console.error("❌ Error al comprimir la imagen:", error);
          this.selectedFile = null;
          this.fileError = true;
        });
    } else {
      console.log("📄 Archivo cargado sin compresión:", file);
      this.selectedFile = file;
      this.fileError = false;
    }
  }

  onSubmit(): void {
    if (this.contentForm.valid) {
      const dto = { ...this.contentForm.value, lessonId: this.lessonId };
      console.log("📤 DTO que voy a enviar:", dto);

      this.ContentService.createContent(dto, this.selectedFile || undefined)
        .subscribe({
          next: (res) => {
            console.log('✅ Contenido creado:', res);
            this.loadContents(); // recargar lista
            this.contentForm.reset({ displayOrder: 1 });
            this.selectedFile = null;
            this.fileError = false;
          },
          error: (err) => console.error('❌ Error al crear contenido:', err)
        });
    } else {
      console.warn("⚠️ Formulario inválido");
    }
  }

  goBack(): void {
    this.router.navigate(['/lessons', this.lessonId, 'contents']);
  }
}
