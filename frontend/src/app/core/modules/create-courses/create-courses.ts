import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-create-courses',
  templateUrl: './create-courses.html',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  styleUrls: ['./create-courses.css']
})
export class CreateCourses implements OnInit {
  courseForm!: FormGroup; // Definimos el formulario
showSuccessMessage: any;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    // Inicializamos el formulario con FormBuilder y los validadores necesarios
    this.courseForm = this.fb.group({
      title: ['', Validators.required], // Título obligatorio
      description: ['', Validators.required], // Descripción obligatoria
      price: [null, [Validators.required, Validators.min(0)]], // Precio obligatorio y mayor que 0
      coverUrl: ['', Validators.required], // URL de portada obligatoria
      semester: [null, [Validators.required, Validators.min(0)]], // Semestre obligatorio y mayor que 0
      priorKnowledge: ['', Validators.required], // Conocimientos previos obligatorios
      estimatedDurationMinutes: [null, [Validators.required, Validators.min(1)]], // Duración obligatoria y mayor que 0
    });
  }

  // Método para obtener los controles del formulario de forma más fácil
  get f() {
    return this.courseForm.controls;
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

