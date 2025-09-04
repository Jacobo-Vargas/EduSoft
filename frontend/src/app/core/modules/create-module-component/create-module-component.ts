import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModuleService } from '../../services/module.service';

@Component({
  selector: 'app-create-module',
  standalone:false,
  templateUrl: './create-module-component.html',
  styleUrls: ['./create-module-component.css']
})
export class CreateModuleComponent implements OnInit {
  moduleForm!: FormGroup;
  message: string | null = null;
  courseId!: number;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private moduleService: ModuleService
  ) {}

  ngOnInit(): void {
    // obtener el courseId desde la URL
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));

    // inicializar formulario
    this.moduleForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(120)]],
      description: ['', [Validators.maxLength(800)]],
      displayOrder: ['', Validators.required],
      isVisible: [true]
    });
  }

  onSubmit(): void {
    if (this.moduleForm.invalid) return;

    const payload = {
      ...this.moduleForm.value,
      courseId: this.courseId
    };

    this.moduleService.createModule(payload).subscribe({
      next: () => {
        this.message = '✅ Módulo creado con éxito';
        // redirigir a la lista de módulos del curso
        this.router.navigate(['/modules', this.courseId]);
      },
      error: (err) => {
        this.message = '❌ Error al crear el módulo';
        console.error(err);
      }
    });
  }
}
