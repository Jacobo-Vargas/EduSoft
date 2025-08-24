import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CRUDService } from '../../../services/crud.service';
import { UserInfoService } from '../../../services/user-info.service';

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
    private cdRef: ChangeDetectorRef,
    public userInfoService: UserInfoService
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
    // Sincroniza dataForm desde userInfoService si no tiene datos cargados
    if (!this.crudService.dataForm?.fineractId) {
      this.crudService.setUserDataFromUserInfo();
    }

    // Si aún no hay fineractId, intenta obtenerlo desde localStorage
    if (!this.crudService.dataForm?.fineractId) {
      const idFromStorage = localStorage.getItem('fineractId');
      if (idFromStorage) {
        this.crudService.dataForm.fineractId = idFromStorage;
        if (this.userInfoService.userInfo) {
          this.userInfoService.userInfo.fineractId = idFromStorage;
        }
      }
    }
    this.userInfoService.fineractId = this.crudService.dataForm?.fineractId || '';
    this.cdRef.detectChanges(); // fuerza render actualizado
  }

  get country(): string {
    return this.crudService.dataForm?.countryId ?? '';
  }

  get userName(): string {
    if (!this.crudService.dataForm?.firstName) {
      this.crudService.setUserDataFromUserInfo();
    }
    return this.crudService.dataForm?.firstName || '';
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
