import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CourseService } from '../../services/course-service';

export interface CategorieResponseDTO {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
}

@Component({
  selector: 'app-create-courses',
  templateUrl: './create-courses.html',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  styleUrls: ['./create-courses.css']
})

export class CreateCourses implements OnInit {
  courseForm!: FormGroup; // Definimos el formulario
showSuccessMessage: any;
categories: CategorieResponseDTO[] = [];

  constructor(private fb: FormBuilder,private courseService: CourseService) { }

  ngOnInit(): void {

    this.courseService.getCategories().subscribe({
    next: (cats) => this.categories = cats,
    error: () => {/* manejar error */}
  });

    // Inicializamos el formulario con FormBuilder y los validadores necesarios
    this.courseForm = this.fb.group({
      title: ['', Validators.required], // Título obligatorio
      description: ['', Validators.required], // Descripción obligatoria
      price: [null, [Validators.required, Validators.min(0)]], // Precio obligatorio y mayor que 0
      coverUrl: ['', Validators.required], // URL de portada obligatoria
      semester: [null, [Validators.required, Validators.min(0)]], // Semestre obligatorio y mayor que 0
      priorKnowledge: [''], // Conocimientos previos obligatorios
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
    if (this.courseForm.invalid) {
      // Si el formulario es inválido, marca todos los controles como tocados
      Object.values(this.courseForm.controls).forEach(c => c.markAsTouched());
      return;
    }

    // Si el formulario es válido, puedes manejar los datos aquí
    console.log('Formulario enviado con los siguientes datos:', this.courseForm.value);

    // Aquí puedes llamar a tu servicio para registrar el curso, por ejemplo:
    // this.courseService.createCourse(this.courseForm.value).subscribe(response => {
    //   console.log('Curso creado exitosamente:', response);
    // });

  }
}

