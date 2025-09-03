import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CourseService, courseResponseDTO } from '../../services/course-service';

@Component({
  selector: 'app-teacher',
  templateUrl: './teacher.html',
  styleUrls: ['./teacher.css']
})
export class TeacherComponent implements OnInit {

  cursos: courseResponseDTO[] = [];

  constructor(private router: Router, private courseService: CourseService) {}

  ngOnInit(): void {
    const userId = 1; // ⚡ Aquí debes poner el ID real del profesor logueado
    this.courseService.getCoursesByUser(userId).subscribe({
      next: (res) => {
        this.cursos = res;
      },
      error: (err) => {
        console.error('Error cargando cursos:', err);
      }
    });
  }

  verCurso(curso: courseResponseDTO) {
    console.log('Ver curso:', curso);
    // Redirige al detalle del curso
    // this.router.navigate(['/curso', curso.id]);
  }

  crearCurso() {
    this.router.navigate(['/app-create-courses']);
  }
}
