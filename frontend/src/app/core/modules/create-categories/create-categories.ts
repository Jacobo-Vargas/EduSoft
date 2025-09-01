import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-create-categories',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './create-categories.html',
  styleUrl: './create-categories.css'
})
export class CreateCategories {
onSubmit() {
throw new Error('Method not implemented.');
}
  categorieForm!: FormGroup; // Definimos el formulario
  showSuccessMessage: any;
    
  constructor(private fb: FormBuilder) { }

  // Método para obtener los controles del formulario de forma más fácil
  get f() {
    return this.categorieForm.controls;
  }

}
