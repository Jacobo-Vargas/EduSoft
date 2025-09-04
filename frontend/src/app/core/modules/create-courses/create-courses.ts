
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

  constructor(
    public fb: FormBuilder, 
    public courseService: CourseService,
    private router: Router) { }

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
onFileSelected(event: any) {
  this.selectedFile = event.target.files[0];
}
}

