import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../services/course-service';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';

export interface CategorieResponseDTO {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
}

@Component({
  selector: 'app-create-courses',
  templateUrl: './create-courses.html',
  styleUrls: ['./create-courses.css'],
  standalone: false
})
export class CreateCourses implements OnInit {
  @Input() tituloFormulario: string = 'Registrar Curso';
  @Input() textoBoton: string = 'Registrar Curso';

  courseForm!: FormGroup;
  categories: CategorieResponseDTO[] = [];
  selectedFile: File | null = null;
  imageError: string | null = null;
  isLoading: boolean = false;
  previewUrl: string | ArrayBuffer | null = null;

  constructor(
    public fb: FormBuilder,
    public courseService: CourseService,
    private router: Router,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.loadCategories();

    this.courseForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: [''],
      semester: [null, [Validators.required, Validators.min(0), Validators.max(13)]],
      priorKnowledge: [''],
      estimatedDurationMinutes: [null, [Validators.required, Validators.min(1)]],
      categoryId: [null, Validators.required]
    });

    const semesterCtrl = this.courseForm.get('semester')!;
    const pkCtrl = this.courseForm.get('priorKnowledge')!;
    semesterCtrl.valueChanges.subscribe((val) => {
      if (Number(val) !== 0) {
        pkCtrl.setValidators([Validators.required, Validators.minLength(3)]);
      } else {
        pkCtrl.clearValidators();
      }
      pkCtrl.updateValueAndValidity({ emitEvent: false });
    });
  }

  private loadCategories(): void {
    this.courseService.getCategories().subscribe({
      next: (cats) => this.categories = cats,
      error: (err) => {
        console.error('Error cargando categorías:', err);
        this.alertService.createAlert('No se pudieron cargar las categorías', 'error', false);
      }
    });
  }

  get f() {
    return this.courseForm.controls;
  }

  get isPriorKnowledgeRequired(): boolean {
    return Number(this.courseForm.get('semester')?.value ?? 0) !== 0;
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) {
      this.selectedFile = null;
      this.previewUrl = null;
      return;
    }

    if (!file.type.match(/image\/(png|jpeg)/)) {
      this.imageError = 'Solo se permiten imágenes PNG o JPEG';
      this.previewUrl = null;
      return;
    }

    if (file.size > 300 * 1024) {
      this.imageError = 'La imagen no debe superar los 300 KB';
      this.previewUrl = null;
      return;
    }

    this.imageError = null;
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result;
    reader.readAsDataURL(file);
  }

  onSubmit(): void {
    if (this.courseForm.invalid || !this.selectedFile) {
      Object.values(this.courseForm.controls).forEach(c => c.markAsTouched());
      this.alertService.createAlert('Formulario inválido o falta imagen', 'warning', false);
      return;
    }

    this.isLoading = true;
    const formData = new FormData();
    formData.append('title', this.courseForm.value.title);
    formData.append('description', this.courseForm.value.description);
    formData.append('price', this.courseForm.value.price || 0);
    formData.append('semester', this.courseForm.value.semester);
    formData.append('priorKnowledge', this.courseForm.value.priorKnowledge);
    formData.append('estimatedDurationMinutes', this.courseForm.value.estimatedDurationMinutes);
    formData.append('categoryId', this.courseForm.value.categoryId);
    formData.append('userId', '1'); // ⚡ cambiar por usuario real
    formData.append('coverUrl', this.selectedFile);

    this.courseService.createCourse(formData).subscribe({
      next: () => {
        this.isLoading = false;
        this.alertService.createAlert('✅ Curso creado con éxito', 'success', false).then(() => {
          this.courseForm.reset();
          this.selectedFile = null;
          this.router.navigate(['/teacher']);
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error al crear curso:', err);
        const msg = err?.error?.message || 'Error inesperado al crear el curso';
        this.alertService.createAlert(`❌ ${msg}`, 'error', false);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/teacher']);
  }
}
