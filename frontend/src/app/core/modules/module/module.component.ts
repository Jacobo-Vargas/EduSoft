import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ModuleService, ModuleResponseDto } from '../../services/module.service';
import { AuthService, UserData } from '../../services/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-module',
  standalone: false,
  templateUrl: './module.component.html',
  styleUrls: ['./module.component.css']
})
export class ModuleComponent implements OnInit, OnDestroy {
  modules: ModuleResponseDto[] = [];
  loading = true;
  error: string | null = null;
  userData: UserData | null = null;
  courseId!: number;

  private subscriptions: Subscription[] = [];

  constructor(
    private moduleService: ModuleService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // obtener el id del curso de la URL
    this.courseId = Number(this.route.snapshot.paramMap.get('courseId'));
    console.log('📌 courseId obtenido de la URL:', this.courseId);

    // cargar datos del usuario autenticado
    const userDataSub = this.authService.getUserData().subscribe({
      next: (userData) => {
        console.log('📌 userData recibido desde AuthService:', userData);
        this.userData = userData;

        if (userData) {
          console.log('✅ Usuario válido, cargando módulos del curso...');
          this.loadModules();
        } else {
          this.error = 'No se encontraron datos del usuario';
          this.loading = false;
          console.warn('⚠ No se encontraron datos de usuario en AuthService');
        }
      },
      error: (err) => {
        this.error = 'Error obteniendo datos de usuario';
        this.loading = false;
        console.error('❌ Error al obtener datos de usuario:', err);
      }
    });

    this.subscriptions.push(userDataSub);
  }

  private loadModules(): void {
    console.log(`📡 Solicitando módulos para courseId=${this.courseId}`);

    const modulesSub = this.moduleService.getModulesByCourse(this.courseId).subscribe({
      next: (data) => {
        console.log('✅ Respuesta recibida del backend:', data);
        this.modules = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los módulos';
        this.loading = false;
        console.error('❌ Error al cargar módulos desde backend:', err);
      }
    });

    this.subscriptions.push(modulesSub);
  }

  deleteModule(module: ModuleResponseDto) {
    if (!confirm(`¿Deseas eliminar el módulo "${module.name}"?`)) return;

    this.moduleService.deleteModule(module.id).subscribe({
      next: () => {
        this.modules = this.modules.filter(m => m.id !== module.id);
        console.log(`Módulo ${module.name} eliminado`);
      },
      error: (err) => {
        console.error('Error eliminando módulo:', err);
        alert('No se pudo eliminar el módulo');
      }
    });
  }

  ngOnDestroy(): void {
    console.log('🧹 Liberando suscripciones en ModuleComponent');
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
