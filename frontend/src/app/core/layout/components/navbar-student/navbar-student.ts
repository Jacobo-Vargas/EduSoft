import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { AlertService } from '../../../services/alert.service';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { userResponseDTO, UsuarioService } from '../../../services/usuario.service';

@Component({
  selector: 'app-navbar-student',
  standalone: false,
  templateUrl: './navbar-student.html',
  styleUrl: './navbar-student.css'
})
export class NavbarStudent implements OnInit {
  name: string = '';
  coverUrl: string = '';
  previewUrl: string | ArrayBuffer | null = null;
  actualizarFoto: boolean = false;


  constructor(public authService: AuthService, private alertService: AlertService, public router: Router, public translate: TranslateService, private userService: UsuarioService) {
    this.translate.use('es');
  }
  ngOnInit(): void {
    // Suscribirse al observable compartido
    this.userService.userData$.subscribe((res) => {
      if (res && res.datos) {
        const fullName = res.datos.name ?? '';
        this.name = fullName.split(' ')[0];
        this.previewUrl = res.datos.coverUrl
          ? res.datos.coverUrl
          : `https://ui-avatars.com/api/?name=${encodeURIComponent(res.datos.name || 'Usuario')}&background=0D8ABC&color=fff&size=128`;
      } else {
        this.actualizarFoto = true;
      }
    });

    if (this.actualizarFoto) {
      this.userService.getInformationUser().subscribe({
        next: (res: userResponseDTO) => {
          if (res && res.datos) {
            const fullName = res.datos.name ?? '';
            this.name = fullName.split(' ')[0];
          }
          if (res.datos.coverUrl) {
            this.previewUrl = res.datos.coverUrl;
          } else {
            this.previewUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(res.datos.name || 'Usuario')}&background=0D8ABC&color=fff&size=128`;
          }
        },
        error: (err) => {
          console.error("❌ Error al obtener información del usuario:", err);
        }
      });
    }
  }

  logout() {
    this.authService.logout();
  }



}
