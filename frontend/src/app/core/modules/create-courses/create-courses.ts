
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../services/course-service';
import { Router } from '@angular/router';

export interface CategorieResponseDTO {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
}

export interface courseRequestDTO {
  id: number;
  title: string;
  description: String;
  price: number;
  coverUrl: String;
  semester: number;
  priorKnowledge: String;
  estimatedDurationMinutes: number;
  categoryId: number;
  userId: string;
  imageError: string | null ;



}
@Component({
  selector: 'app-create-courses',
  templateUrl: './create-courses.html',
  styleUrls: ['./create-courses.css'],
  standalone: false
})

export class CreateCourses implements OnInit {
  courseForm!: FormGroup; // Definimos el formulario
  showSuccessMessage: any;
  categories: CategorieResponseDTO[] = [];
  selectedFile: File | null = null;
  imageError: string | null = null;
 
  constructor(
    public fb: FormBuilder, 
    public courseService: CourseService,
    private router: Router) { }

    // === Parámetros configurables ===
private readonly TARGET_W = 800;
private readonly TARGET_H = 600;
private readonly MAX_SIZE_BYTES = 300 * 1024; // 300 KB
private readonly OUTPUT_MIME = 'image/jpeg'; // comprimimos a JPG
private readonly QUALITY_START = 0.9;
private readonly QUALITY_MIN = 0.5;
private readonly QUALITY_STEP = 0.1;

  ngOnInit(): void {

    this.courseService.getCategories().subscribe({
      next: (cats) => this.categories = cats,
      error: () => {/* manejar error */ }
    });

    // Inicializamos el formulario con FormBuilder y los validadores necesarios
    this.courseForm = this.fb.group({
      title: ['', Validators.required], // Título obligatorio
      description: ['', Validators.required], // Descripción obligatoria
      price: [null, [Validators.required, Validators.min(0)]], // Precio obligatorio y mayor que 0
    
      semester: [null, [Validators.required, Validators.min(0)]], // Semestre obligatorio y mayor que 0
      priorKnowledge: [''],
      estimatedDurationMinutes: [null, [Validators.required, Validators.min(1)]],
      categoryId: [null, Validators.required] // Duración obligatoria y mayor que 0
    });

    // Cuando cambie semester, (des)activa el required en priorKnowledge
    const semesterCtrl = this.courseForm.get('semester')!;
    const pkCtrl = this.courseForm.get('priorKnowledge')!;

    // Inicializa + escucha cambios
    const applyRule = (semVal: any) => {
      const sem = Number(semVal ?? 0);
      if (sem !== 0) {
        pkCtrl.setValidators([Validators.required, Validators.minLength(3)]);
      } else {
        pkCtrl.clearValidators();
      }
      pkCtrl.updateValueAndValidity({ emitEvent: false });
    };

    applyRule(semesterCtrl.value);
    semesterCtrl.valueChanges.subscribe(applyRule);
  }

  // Método para obtener los controles del formulario de forma más fácil
  get f() {
    return this.courseForm.controls;
  }
  get isPriorKnowledgeRequired(): boolean {
    return Number(this.courseForm.get('semester')?.value ?? 0) !== 0;
  }

 onSubmit(): void {
  if (this.courseForm.invalid || !this.selectedFile) {
    Object.values(this.courseForm.controls).forEach(c => c.markAsTouched());
    return;
  }

  const formData = new FormData();
  formData.append('title', this.courseForm.value.title);
  formData.append('description', this.courseForm.value.description);
  formData.append('price', this.courseForm.value.price);
  formData.append('semester', this.courseForm.value.semester);
  formData.append('priorKnowledge', this.courseForm.value.priorKnowledge);
  formData.append('estimatedDurationMinutes', this.courseForm.value.estimatedDurationMinutes);
  formData.append('categoryId', this.courseForm.value.categoryId);
  formData.append('userId', '1'); // ⚡ cámbialo por el usuario autenticado
  formData.append('coverUrl', this.selectedFile); // archivo real

  this.courseService.createCourse(formData).subscribe({
    next: () => {
      this.showSuccessMessage = true;
      this.courseForm.reset();
      this.selectedFile = null;
      setTimeout(() => this.router.navigate(['/teacher']), 2000);
    },
    error: (err) => {
      console.error('Error al crear el curso:', err);
    }
  });
}
    goBack(): void {
   this.router.navigate(['/teacher']);
}

async onFileSelected(event: any) {
  const file: File = event.target.files[0];
  this.imageError = null;
  this.selectedFile = null;

  if (!file) return;

  const allowed = ['image/png', 'image/jpeg'];
  if (!allowed.includes(file.type)) {
    this.imageError = 'Solo se permiten imágenes PNG o JPG';
    return;
  }

  try {
    const img = await this.loadImageFromFile(file);

    // Lienzo en 800x600 (cover)
    const canvas = this.drawCover(img, this.TARGET_W, this.TARGET_H);

    // Comprimir hasta alcanzar MAX_SIZE_BYTES
    const blob = await this.compressToMaxSize(canvas, this.OUTPUT_MIME, this.MAX_SIZE_BYTES, this.QUALITY_START, this.QUALITY_MIN, this.QUALITY_STEP);

    if (!blob) {
      this.imageError = 'No se pudo comprimir la imagen. Intenta con otra.';
      return;
    }

    // Archivo final (JPG)
    this.selectedFile = new File([blob], `cover_${Date.now()}.jpg`, { type: this.OUTPUT_MIME });
  } catch (e) {
    console.error(e);
    this.imageError = 'No se pudo procesar la imagen seleccionada.';
  }
}

// === Utils ===
private loadImageFromFile(file: File): Promise<HTMLImageElement> {
  return new Promise((resolve, reject) => {
    const url = URL.createObjectURL(file);
    const img = new Image();
    img.onload = () => {
      URL.revokeObjectURL(url);
      resolve(img);
    };
    img.onerror = (err) => {
      URL.revokeObjectURL(url);
      reject(err);
    };
    img.src = url;
  });
}

/**
 * Dibuja la imagen "cubriendo" el área target (como object-fit: cover):
 * llena 800x600 y recorta el exceso manteniendo proporciones.
 */
private drawCover(img: HTMLImageElement, targetW: number, targetH: number): HTMLCanvasElement {
  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d')!;
  canvas.width = targetW;
  canvas.height = targetH;

  // Relleno blanco (por si la fuente era PNG con transparencia)
  ctx.fillStyle = '#ffffff';
  ctx.fillRect(0, 0, targetW, targetH);

  const srcW = img.naturalWidth;
  const srcH = img.naturalHeight;

  const scale = Math.max(targetW / srcW, targetH / srcH); // cover
  const drawW = srcW * scale;
  const drawH = srcH * scale;

  const dx = (targetW - drawW) / 2;
  const dy = (targetH - drawH) / 2;

  ctx.drawImage(img, dx, dy, drawW, drawH);
  return canvas;
}

private canvasToBlob(canvas: HTMLCanvasElement, type: string, quality?: number): Promise<Blob | null> {
  return new Promise((resolve) => {
    canvas.toBlob((blob) => resolve(blob), type, quality);
  });
}

/**
 * Intenta reducir la calidad de JPG hasta quedar <= maxBytes.
 * Si en QUALITY_MIN aún se pasa, retorna el último intento.
 */
private async compressToMaxSize(
  canvas: HTMLCanvasElement,
  mime: string,
  maxBytes: number,
  qualityStart: number,
  qualityMin: number,
  step: number
): Promise<Blob | null> {
  let q = qualityStart;
  let lastBlob: Blob | null = null;

  while (q >= qualityMin) {
    const blob = await this.canvasToBlob(canvas, mime, q);
    if (!blob) return null;
    lastBlob = blob;

    if (blob.size <= maxBytes) return blob;
    q = Number((q - step).toFixed(2));
  }

  // Si no se alcanzó el tamaño, devolvemos el "mejor" intento
  return lastBlob;
}


}

