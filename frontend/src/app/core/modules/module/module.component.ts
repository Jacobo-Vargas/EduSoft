import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ModuleService, ModuleResponseDto, CategorieResponseDTO, courseResponseDTO } from '../../services/module.service';
import { AuthService, UserData } from '../../services/auth.service';
import { Subscription } from 'rxjs';
import { AlertService } from '../../services/alert.service';
import { CourseService } from '../../services/course.service';

@Component({
  selector: 'app-module',
  standalone: false,
  templateUrl: './module.component.html',
  styleUrls: ['./module.component.css']
})
export class ModuleComponent implements OnInit, OnDestroy {

  modules: ModuleResponseDto[] = [];
  loading = false;
  error: string | null = null;
  userData: UserData | null = null;
  courseId!: number;
  lessonId!: number;
  categories: CategorieResponseDTO[] = [];
  courseData: courseResponseDTO | null = null;

  private subscriptions: Subscription[] = [];

  constructor(
    private moduleService: ModuleService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private courseService: CourseService,
    private router: Router,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.loading = true;

    // Obtener courseId de la URL
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));
    this.lessonId = Number(this.route.snapshot.paramMap.get('lessonId'));
    // Cargar categorías
    const catSub = this.moduleService.getCategories().subscribe({
      next: (cats) => this.categories = cats,
      error: () => { "Error la cargar categorías"}
    });
    this.subscriptions.push(catSub);

   const courseSub = this.moduleService.getCourseById(this.courseId).subscribe({
  next: (response: any) => { // Recibe el objeto completo de respuesta
    this.courseData = response.body; // Accede a la propiedad body
    console.log('📌 Datos del curso obtenidos:', this.courseData);
    // Ahora this.courseData tiene el objeto que esperas: {auditStatusId: 2, ...}
  },
  error: (err) => this.handleError('Error obteniendo curso', err)
});
this.subscriptions.push(courseSub);
   

    // Cargar datos del usuario autenticado
    const userDataSub = this.authService.getUserData().subscribe({
      next: (userData) => {
        this.userData = userData;

        if (userData) {
          this.loadModules();
        } else {
          this.handleError('No se encontraron datos del usuario');
        }
      },
      error: (err) => this.handleError('Error obteniendo datos de usuario', err)
    });
    this.subscriptions.push(userDataSub);
  }

  private loadModules(): void {
    this.loading = true;

    const modulesSub = this.moduleService.getModulesByCourse(this.courseId).subscribe({
      next: (data) => {
        this.modules = data;
        this.loading = false;
      },
      error: (err) => this.handleError('Error al cargar los módulos', err)
    });
    this.subscriptions.push(modulesSub);
  }

  deleteModule(module: ModuleResponseDto) {
    this.alertService.confirmCustomAlert(
      `¿Deseas eliminar el módulo "${module.name}"?`,
      'question',
      this.alertService.jsonData['alert']?.['btnAccept'] || 'Aceptar',
      this.alertService.jsonData['alert']?.['btnCancel'] || 'Cancelar'
    ).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;
      const sub = this.moduleService.deleteModule(module.id).subscribe({
        next: () => {
          this.modules = this.modules.filter(m => m.id !== module.id);
          this.alertService.createAlert(
            `Módulo "${module.name}" eliminado correctamente`,
            'success',
            false
          );
          this.loading = false;
        },
        error: (err) => this.handleError('No se pudo eliminar el módulo', err)
      });
      this.subscriptions.push(sub);
    });
  }

  submitForAudit() {
    this.alertService.confirmCustomAlert(
      `¿Deseas enviar el curso a auditoría?`,
      'question',
      this.alertService.jsonData['alert']?.['btnAccept'] || 'Aceptar',
      this.alertService.jsonData['alert']?.['btnCancel'] || 'Cancelar'
    ).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;
      this.courseService.setStatusAudit(this.courseId).subscribe({
        next: (res) => {
          this.alertService.createAlert('Curso enviado a auditoría exitosamente', 'success', false)
          this.loading = false;
        },
        error: (err) => this.handleError('No se pudo enviar el curso a auditoría', err)
      });
    });
  }

  private handleError(msg: string, err?: any) {
    const errorMessage = err?.error?.message || msg;
    this.error = errorMessage;
    this.alertService.createAlert(errorMessage, 'error', false);
    console.error('❌', errorMessage, err);
    this.loading = false;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  goBack(): void {
    this.router.navigate(['/teacher']);
  }
}
