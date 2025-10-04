import { Component, OnInit } from '@angular/core';
import { CourseService, courseResponseDTO } from '../../../services/course.service';
import { AlertService } from '../../../services/alert.service';

interface CategoriaCursos {
  nombre: string;
  gruposCursos: courseResponseDTO[][];
}

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  categoriasCursos: CategoriaCursos[] = [];

  constructor(
    private courseService: CourseService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.courseService.getVisibleCourses().subscribe(cursos => {
      this.categoriasCursos = this.agruparPorCategoria(cursos);
    });
  }

  private agruparPorCategoria(cursos: courseResponseDTO[]): CategoriaCursos[] {
    const categoriasMap = new Map<string, courseResponseDTO[]>();

    cursos.forEach(curso => {
      const categoria = curso.categoryName || 'Sin categoría';
      if (!categoriasMap.has(categoria)) {
        categoriasMap.set(categoria, []);
      }
      categoriasMap.get(categoria)!.push(curso);
    });

    return Array.from(categoriasMap.entries()).map(([nombre, cursos]) => {
      const grupos: courseResponseDTO[][] = [];
      for (let i = 0; i < cursos.length; i += 3) {
        grupos.push(cursos.slice(i, i + 3));
      }
      return { nombre, gruposCursos: grupos };
    });
  }

  inscribirse(curso: courseResponseDTO): void {
    const mensaje = `<p>Antes de inscribirte verifica lo siguiente:</p>
                 <p><b>Semestre recomendado:</b> ${curso.semester}<br>
                 <b>Conocimientos previos:</b> ${curso.priorKnowledge || 'Ninguno'}</p>
                 <p>¿Deseas continuar con la inscripción?</p>`;

    // Mostrar modal con botones personalizados usando AlertService
    this.alertService.createAlertHTML(
      'Confirmar inscripción',
      mensaje,
      'info',
      true // true = es confirmación con botones
    ).then(result => {
      if (result.isConfirmed) {
        // El usuario aceptó, inscribir
        this.courseService.enrollToCourse(curso.id).subscribe({
          next: () => {
            this.alertService.createAlert(
              '✅ Te has inscrito correctamente al curso',
              'success',
              false
            );
          },
          error: (err) => {
            const msg = err?.error || '❌ No se pudo realizar la inscripción';
            this.alertService.createAlert(msg, 'error', false);
          }
        });
      } else {
        // Canceló la inscripción
        this.alertService.createAlert(
          '❌ No te has inscrito al curso',
          'info',
          false
        );
      }
    });
  }
}
