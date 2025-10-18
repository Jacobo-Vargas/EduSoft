import { Component, OnInit } from '@angular/core';
import { courseResponseDTO, CourseService, EnrollmentResponseDTO } from '../../services/course.service';
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
    this.loadCourses();
  }

  loadCourses(): void {
    const coursesSub = this.courseService.getCoursesStudent().subscribe({
      next: (res: EnrollmentResponseDTO[]) => {
        this.cursos = res;
        this.loading = false;

        if (!this.cursos || this.cursos.length === 0) {
          this.alertService.createAlert(
            'No estás inscrito en ningún curso. ¡Explora nuestros cursos y regístrate!',
            'info',
            false
          );

          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 3000);
        }
      },
      error: (err: any) => {
        this.alertService.createAlert(
          'Error cargando cursos. Intenta de nuevo más tarde.',
          'error',
          false
        );
        console.error('❌ Error cargando cursos:', err);
        this.loading = false;

        setTimeout(() => {
          this.router.navigate(['/app-student-courses']);
        }, 3000);
      }
    });

    this.subscriptions.push(coursesSub);
}

  verCurso(courseId: number) {
    this.router.navigate(['/ver-curso', courseId]);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  courseUnsubscribe(courseId: number, price: number) {
  // Si el curso tiene precio, mostrar confirmación
  if (price > 0) {
    const mensaje = `
      <p>Este curso tiene un precio de <b>$${price}</b>.</p>
      <p>¿Deseas continuar con la anulacion de suscripcion?</p>
    `;
    this.alertService.createAlertHTML('Confirmar anulacion de suscripcion', mensaje, 'warning', true)
      .then(result => {
        if (result.isConfirmed) {
          this.ejecutarDesinscripcion(courseId);
        } else {
          this.alertService.createAlert('❌ No has anulado la suscripcion del curso', 'info', false);
        }
      });
  } else {
    this.alertService.createAlertHTML('Confirmar anulacion de suscripcion', '¿Deseas continuar con la anulacion de  suscripcion?', 'warning', true)
      .then(result => {
        if (result.isConfirmed) {
          this.ejecutarDesinscripcion(courseId);
        } else {
          this.alertService.createAlert('❌ No has anulado la suscripcion del curso', 'info', false);
        }
      });
  }
}

// Método privado para mantener la lógica limpia
private ejecutarDesinscripcion(courseId: number) {
  this.courseService.courseUnsubscribe(courseId).subscribe({
    next: () => {
      this.alertService.createAlert(
        '✅ Te has desinscrito correctamente del curso.',
        'success',
        true
      );
      this.loadCourses(); // recargar lista
    },
    error: (err) => {
      this.alertService.createAlert(
        '❌ Error al desinscribirse. Intenta de nuevo más tarde.',
        'error',
        false
      );
    }
  });
}



}
