import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CRUDService } from '../../../services/crud.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: false
})

export class HomeComponent implements OnInit {

  cursos: any[] = [];

  gruposCursos: any[][] = [];


  constructor(
    public crudService: CRUDService,
    public translate: TranslateService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.cursos = [
      { titulo: 'Curso de Angular', descripcion: 'Aprende Angular desde cero.', imagen: '../../../assets/img/angular.png' },
      { titulo: 'Curso de Spring Boot', descripcion: 'Construye APIs con Spring.', imagen: '../../../assets/img/spring.png' },
      { titulo: 'Curso de PostgreSQL', descripcion: 'Domina bases de datos relacionales.', imagen: '../../../assets/img/postgres.png' },
      { titulo: 'Curso de Angular', descripcion: 'Aprende Angular desde cero.', imagen: '../../../assets/img/angular.png' },
      { titulo: 'Curso de Spring Boot', descripcion: 'Construye APIs con Spring.', imagen: '../../../assets/img/spring.png' },
      { titulo: 'Curso de PostgreSQL', descripcion: 'Domina bases de datos relacionales.', imagen: '../../../assets/img/postgres.png' }
    ]
    this.crearGruposCursos();
    this.cdRef.detectChanges();
  }

  crearGruposCursos() {
    const temp = [...this.cursos];
    // Si hay menos de 3, repetir cursos hasta tener al menos 3
    while (temp.length < 3) {
      temp.push(...this.cursos);
    }

    // Crear grupos de 3 cursos
    this.gruposCursos = [];
    for (let i = 0; i < temp.length; i += 3) {
      this.gruposCursos.push(temp.slice(i, i + 3));
    }
  }
}
