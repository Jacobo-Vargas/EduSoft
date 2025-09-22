import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CourseService, courseResponseDTO } from '../../../services/course.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: false
})
export class HomeComponent implements OnInit {

  cursos: courseResponseDTO[] = [];
  gruposCursos: courseResponseDTO[][] = [];

  constructor(
    private courseService: CourseService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.courseService.getVisibleCourses().subscribe({
      next: (res) => {
        this.cursos = res;
        this.crearGruposCursos();
        this.cdRef.detectChanges();
      },
      error: (err) => {
        console.error("❌ Error cargando cursos visibles:", err);
      }
    });
  }

  crearGruposCursos() {
    const temp = [...this.cursos];

    // Si hay menos de 3 cursos, repetir hasta tener al menos 3
    while (temp.length < 3) {
      temp.push(...this.cursos);
    }

    this.gruposCursos = [];
    for (let i = 0; i < temp.length; i += 3) {
      this.gruposCursos.push(temp.slice(i, i + 3));
    }
  }
}
