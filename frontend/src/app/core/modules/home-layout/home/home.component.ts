import { Component, OnInit } from '@angular/core';
import { CourseService, courseResponseDTO } from '../../../services/course.service';
import { AlertService } from '../../../services/alert.service';
import Swal from 'sweetalert2';
import { PaymentService } from '../../../services/payment.service';

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
    const mensaje = `
    <p>Antes de inscribirte verifica lo siguiente:</p>
    <p><b>Semestre recomendado:</b> ${curso.semester}</p>
    <p><b>Precio del curso:</b> ${curso.price > 0 ? '$' + curso.price : 'Gratis'}</p>
    <p><b>Conocimientos previos:</b> ${curso.priorKnowledge || 'Ninguno'}</p>
    <p>¿Deseas continuar con la inscripción?</p>
  `;

    // Primer modal: confirmar inscripción
    this.alertService.createAlertHTML('Confirmar inscripción', mensaje, 'info', true)
      .then(result => {
        if (result.isConfirmed) {
          // Si el curso tiene precio, mostramos otra alerta para confirmar pago
          if (curso.price > 0) {
            const pagoMensaje = `
            <p>El curso tiene un costo de <b>$${curso.price}</b>.</p>
            <p>¿Deseas proceder con el pago e inscribirte?</p>`;
            this.alertService.createAlertHTML('Confirmar pago', pagoMensaje, 'info', true)
              .then(resPago => {

                if (resPago.isConfirmed) {

                  this.courseService.getInitpoint(curso.id).subscribe({
                    next: (res) => {
                      window.location.href = res.init_point;
                    },
                    error: (err) => {
                      const msg = err?.error?.error || '❌ No se pudo iniciar el pago';
                      this.alertService.createAlert(msg, 'error', false);
                    }
                  });


                } else {
                  this.alertService.createAlert('❌ Inscripción cancelada', 'info', false);
                }
              });
          } else {
            // Curso gratuito, inscribimos directamente
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
          }
        } else {
          this.alertService.createAlert('❌ Inscripción cancelada', 'info', false);
        }
      });
  }
}
