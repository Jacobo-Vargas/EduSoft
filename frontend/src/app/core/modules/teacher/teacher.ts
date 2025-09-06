import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { CourseService } from '../../services/course-service';
import { AuthService, UserData } from '../../services/auth.service';
import { Subscription } from 'rxjs';
export interface courseResponseDTO {
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
  selector: 'app-teacher',
  templateUrl: './teacher.html',
  styleUrls: ['./teacher.css'],
  standalone: false
})
export class TeacherComponent implements OnInit, OnDestroy {
  cursos: courseResponseDTO[] = [];
  userData: UserData | null = null;

  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private courseService: CourseService,
    private authService: AuthService
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
    // Usar el ID real del usuario desde el AuthService
    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      console.error("❌ No se pudo obtener el ID del usuario");
      return;
    }

    const coursesSub = this.courseService.getCoursesByUser(userId).subscribe({
      next: (res) => {
        this.cursos = res;
        console.log("📚 Cursos cargados:", this.cursos);
      },
      error: (err) => {
        console.error("❌ Error cargando cursos:", err);
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
    this.router.navigate(['/app-create-courses'], {
      state: { userData: this.userData }
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
  editarCurso(curso: courseResponseDTO) {
    this.router.navigate(['/app-edit-course', curso.id], {
      state: { userData: this.userData, curso }
    });
  }

  eliminarCurso(curso: courseResponseDTO) {
  if (confirm(`¿Deseas eliminar el curso "${curso.title}"?`)) {
    this.courseService.deleteCourse(curso.id).subscribe({
      next: () => {
        this.cursos = this.cursos.filter(c => c.id !== curso.id);
        console.log("Curso eliminado:", curso.id);
      },
      error: (err) => console.error("Error eliminando curso:", err)
    });
  }
}


  

}
