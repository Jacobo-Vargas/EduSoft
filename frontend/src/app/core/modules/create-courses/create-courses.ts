import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { courseResponseDTO, CourseService } from '../../services/course-service';
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
export class CreateCourses implements OnInit, OnChanges {
  @Input() tituloFormulario: string = 'Registrar Curso';
  @Input() textoBoton: string = 'Registrar Curso';
  @Input() usarFondo: boolean = true;
  @Input() cursoEditar: courseResponseDTO | null = null;


  courseForm!: FormGroup;
  categories: CategorieResponseDTO[] = [];
  selectedFile: File | null = null;
  imageError: string | null = null;
  isLoading: boolean = false;
  previewUrl: string | ArrayBuffer | null = null;
  userData: any = null;

  constructor(
    public fb: FormBuilder,
    public courseService: CourseService,
    private router: Router,
    private alertService: AlertService
  ) {  const navigation = this.router.getCurrentNavigation();
    this.userData = navigation?.extras.state?.['username'] || '';}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['cursoEditar'] && this.cursoEditar) {
      console.log('📌 Detectado cursoEditar:', this.cursoEditar);

      // ✅ Parchar los valores al formulario
      this.courseForm.patchValue({
        title: this.cursoEditar.title,
        description: this.cursoEditar.description,
        price: this.cursoEditar.price,
        semester: this.cursoEditar.semester,
        priorKnowledge: this.cursoEditar.priorKnowledge,
        estimatedDurationMinutes: this.cursoEditar.estimatedDurationMinutes,
        categoryId: this.cursoEditar.categoryId,
        coverUrl: this.cursoEditar.coverUrl,
        id: this.cursoEditar.id,
        userId: this.cursoEditar.id
       
      });
  console.log('📌 Parchando userId:', this.cursoEditar.id);

      if (this.cursoEditar?.coverUrl) {
        this.previewUrl = this.cursoEditar.coverUrl; // ✅ muestra portada existente
      }


    }
  }

  ngOnInit(): void {
    const navigation = this.router.getCurrentNavigation();
    
    this.loadCategories();


    this.courseForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: [''],
      semester: [null, [Validators.required, Validators.min(0), Validators.max(13)]],
      priorKnowledge: [''],
      estimatedDurationMinutes: [null, [Validators.required, Validators.min(1)]],
      categoryId: [null, Validators.required],
      coverUrl: [null],
      formFile: ['']
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

  async onSubmit(): Promise<void> {
    if (this.courseForm.invalid) {
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
    formData.append('userId', this.cursoEditar ? String(this.cursoEditar.id) :  ''); 
    localStorage.clear();
    if (this.selectedFile) {
    // ✅ Usuario seleccionó un nuevo archivo
    formData.append('coverUrl', this.selectedFile);
  } else if (this.cursoEditar?.coverUrl) {
    // ✅ Convertir la URL en un File para que el backend lo acepte
    const file = await this.urlToFile(this.cursoEditar.coverUrl, "portada.jpg");
    formData.append('coverUrl', file);
  }

   if (this.cursoEditar) {
    // 🔄 Actualizar curso
    this.courseService.updateCourse(this.cursoEditar.id!, formData).subscribe({
      next: () => {
        this.isLoading = false;
        this.alertService.createAlert('✅ Curso actualizado con éxito', 'success', false).then(() => {
          this.courseForm.reset();
          this.selectedFile = null;
          this.router.navigate(['/teacher']);
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error al actualizar curso:', err);
        const msg = err?.error?.message || 'Error inesperado al actualizar el curso';
        this.alertService.createAlert(`❌ ${msg}`, 'error', false);
      }
    });
  } else {
    // 🆕 Crear curso
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

  }
  goBack(): void {
    this.router.navigate(['/teacher']);
  }
  get canSave(): boolean {
    // Si el formulario es inválido, nunca habilitar
    if (this.courseForm.invalid) return false;

    // Si es edición y ya existe curso, no exigir archivo nuevo
    if (this.cursoEditar) return true;

    // Si es creación, exigir archivo seleccionado
    return !!this.selectedFile;
  }
  async urlToFile(url: string, filename: string): Promise<File> {
  const response = await fetch(url);
  const blob = await response.blob();
  return new File([blob], filename, { type: blob.type });
}
}