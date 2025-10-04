import { Component, OnInit } from '@angular/core';
import { courseResponseDTO, CourseService, EnrollmentResponseDTO } from '../../services/course.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-student-courses',
  standalone: false,
  templateUrl: './student-courses.html',
  styleUrl: './student-courses.css'
})
export class StudentCourses implements OnInit {

  cursos: EnrollmentResponseDTO[] = [];
  loading = false;
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private courseService: CourseService,
    private authService: AuthService,
    private alertService: AlertService
  ) { }

 ngOnInit(): void {
  this.loading = true;

  const coursesSub = this.courseService.getCoursesStudent().subscribe({
    next: (res: EnrollmentResponseDTO[]) => {
      this.cursos = res;
      this.loading = false;

      // Si no hay cursos, mostrar alerta e invitar al usuario a inscribirse
      if (!this.cursos || this.cursos.length === 0) {
        this.alertService.createAlert(
          'No estás inscrito en ningún curso. ¡Explora nuestros cursos y regístrate!',
          'info',
          false
        );

        // Redirigir al home después de unos segundos (opcional)
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 3000); // 3 segundos
      }
    },
    error: (err: any) => {
      this.alertService.createAlert(
        'Error cargando cursos. Intenta de nuevo más tarde.',
        'error',
        false
      );
      console.error("❌ Error cargando cursos:", err);
      this.loading = false;

      // También podrías redirigir al home si ocurre un error grave
      setTimeout(() => {
        this.router.navigate(['/app-student-courses']);
      }, 3000);
    }
  });

  this.subscriptions.push(coursesSub);
}


}
