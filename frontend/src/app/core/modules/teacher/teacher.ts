import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { CourseService, courseResponseDTO } from '../../services/course-service';
import { AuthService, UserData } from '../../services/auth.service';
import { Subscription } from 'rxjs';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.html',
  styleUrls: ['./teacher.css'],
  standalone: false
})
export class TeacherComponent implements OnInit, OnDestroy {
  cursos: courseResponseDTO[] = [];
  userData: UserData | null = null;
  loading = false;

  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private courseService: CourseService,
    private authService: AuthService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    const navigationState = this.router.getCurrentNavigation()?.extras.state;
    if (navigationState && navigationState['userData']) {
      this.userData = navigationState['userData'];
      this.loadUserCourses();
    } else {
      this.getUserDataFromService();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private getUserDataFromService(): void {
    const userDataSub = this.authService.getUserData().subscribe({
      next: (userData) => {
        this.userData = userData;
        if (userData) {
          this.loadUserCourses();
        } else {
          console.warn('⚠ No hay datos de usuario, redirigiendo al login');
          this.router.navigate(['']);
        }
      },
      error: (err) => {
        console.error('❌ Error obteniendo datos del usuario:', err);
        this.router.navigate(['']);
      }
    });
    this.subscriptions.push(userDataSub);
  }

  private loadUserCourses(): void {
    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      console.error("❌ No se pudo obtener el ID del usuario");
      return;
    }

    this.loading = true;
    const coursesSub = this.courseService.getCoursesByUser(userId).subscribe({
      next: (res) => {
        this.cursos = res;
        this.loading = false;
      },
      error: (err) => {
        this.alertService.createAlert('Error cargando cursos', 'error', false);
        console.error("❌ Error cargando cursos:", err);
        this.loading = false;
      }
    });

    this.subscriptions.push(coursesSub);
  }

  verCurso(curso: courseResponseDTO) {
    this.router.navigate(['/modules', curso.id], {
      state: { userData: this.userData, curso: curso }
    });
  }

  crearCurso() {
      this.router.navigate(['/recover-password'], { state: { username: this.userData } });
  }

  editarCurso(curso: courseResponseDTO) {
    this.router.navigate(['/app-edit-course', curso.id], {
      state: { userData: this.userData, curso }
    });
  }

  eliminarCurso(curso: courseResponseDTO) {
    this.alertService.confirmCustomAlert(
      `¿Deseas eliminar el curso "${curso.title}"?`,
      'question',
      this.alertService.jsonData['alert']?.['btnAccept'] || 'Aceptar',
      this.alertService.jsonData['alert']?.['btnCancel'] || 'Cancelar'
    ).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;
      this.courseService.deleteCourse(curso.id).subscribe({
        next: () => {
          this.cursos = this.cursos.filter(c => c.id !== curso.id);
          this.alertService.createAlert(`Curso "${curso.title}" eliminado correctamente`, 'success', false);
          this.loading = false;
        },
        error: (err) => {
          this.alertService.createAlert('No se pudo eliminar el curso', 'error', false);
          console.error("Error eliminando curso:", err);
          this.loading = false;
        }
      });
    });
  }

  getUserDisplayName(): string {
    return this.userData?.name || 'Usuario';
  }

  getUserEmail(): string {
    return this.userData?.email || '';
  }

  getUserRole(): string {
    return this.userData?.userType || '';
  }
}
