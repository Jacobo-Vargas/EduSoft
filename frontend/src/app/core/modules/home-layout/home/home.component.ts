import { Component, OnInit } from '@angular/core';
import { CourseService, courseResponseDTO } from '../../../services/course.service';

interface CategoriaCursos {
  nombre: string;
  gruposCursos: courseResponseDTO[][];
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  categoriasCursos: CategoriaCursos[] = [];

  constructor(private courseService: CourseService) {}

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
}
