import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { CourseService, courseResponseDTO } from '../../services/course-service';
import { AuthService, UserData } from '../../services/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.html',
  styleUrls: ['./teacher.css']
})
export class TeacherComponent implements OnInit, OnDestroy {
  cursos: courseResponseDTO[] = [];
  userData: UserData | null = null;
  
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router, 
    private courseService: CourseService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Opción 1: Obtener datos del estado de navegación (si vienen del login)
    const navigationState = this.router.getCurrentNavigation()?.extras.state;
    if (navigationState && navigationState['userData']) {
      this.userData = navigationState['userData'];
      this.loadUserCourses();
    } else {
      // Opción 2: Obtener datos del servicio de autenticación
      this.getUserDataFromService();
    }
  }

  ngOnDestroy(): void {
    // Limpiar suscripciones para evitar memory leaks
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private getUserDataFromService(): void {
    // Suscribirse al Observable de userData
    const userDataSub = this.authService.getUserData().subscribe({
      next: (userData) => {
        this.userData = userData;
        if (userData) {
          this.loadUserCourses();
        } else {
          // Si no hay datos, redirigir al login
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
    if (!this.userData) {
      console.error('❌ No hay datos de usuario para cargar cursos');
      return;
    }

    // TODO: Aquí debes obtener el userId real del userData
    // Por ahora usamos un ID de ejemplo
    const userId = 1; // ⚡ Reemplazar con el ID real del usuario

    const coursesSub = this.courseService.getCoursesByUser(userId).subscribe({
      next: (res) => {
        this.cursos = res;
        console.log('📚 Cursos cargados:', this.cursos);
      },
      error: (err) => {
        console.error('❌ Error cargando cursos:', err);
      }
    });

    this.subscriptions.push(coursesSub);
  }

  verCurso(curso: courseResponseDTO) {
    // Pasar datos del usuario al siguiente componente
    this.router.navigate(['/curso', curso.id], {
      state: { userData: this.userData, curso: curso }
    });
  }

  crearCurso() {
    // Pasar datos del usuario al componente de crear curso
    this.router.navigate(['/app-create-courses'], {
      state: { userData: this.userData }
    });
  }

  // Método para obtener información del usuario (para mostrar en la UI)
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