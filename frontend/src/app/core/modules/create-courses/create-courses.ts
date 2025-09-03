
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../services/course-service';

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
  userId: number;
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

  constructor(public fb: FormBuilder, public courseService: CourseService) { }

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
      coverUrl: ['', Validators.required], // URL de portada obligatoria
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

  // Método que se ejecuta cuando el formulario se envía
  onSubmit(): void {
    console.log(Number(this.courseForm.value.categoryId) + 'probando')
    if (this.courseForm.invalid) {
      // Si el formulario es inválido, marca todos los controles como tocados
      Object.values(this.courseForm.controls).forEach(c => c.markAsTouched());
      return;
    }

    this.courseForm.value.categoryId = Number(this.courseForm.value.categoryId);
    this.courseForm.value.userId = 1; // Aquí deberías obtener el ID del usuario autenticado
    this.courseService.createCourse(this.courseForm.value).subscribe({
      next: () => {
        this.showSuccessMessage = true;
        this.courseForm.reset();
        setTimeout(() => this.showSuccessMessage = false, 5000);
      },
      error: (err) => {
        console.error('Error al crear el curso:', err);
      }
    });


    // Si el formulario es válido, puedes manejar los datos aquí
    console.log('Formulario enviado con los siguientes datos:', this.courseForm.value);

    // Aquí puedes llamar a tu servicio para registrar el curso, por ejemplo:
    // this.courseService.createCourse(this.courseForm.value).subscribe(response => {
    //   console.log('Curso creado exitosamente:', response);
    // });

  }
}

