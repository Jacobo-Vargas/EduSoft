import { Component } from '@angular/core';

@Component({
  selector: 'app-progress',
  standalone: false,
  templateUrl: './progress.component.html',
  styleUrl: './progress.component.css'
})
export class ProgressComponent {
  steps = [1, 2, 3, 4]; // Total de pasos
  currentStep = 0; // Cambia este valor para ver el progreso dinámico
}
