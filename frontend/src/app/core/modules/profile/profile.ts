import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { userResponseDTO, UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

   userDataForm!: FormGroup;
   previewUrl: string | ArrayBuffer | null = null;
   imageError: string | null = null;
   isLoading: boolean = false;
   selectedFile: File | null = null;
   cambiarFoto: boolean = false;
   user: any = null;

  constructor(
    private router: Router, 
    private alertService: AlertService,
    public fb: FormBuilder,
    private userService: UsuarioService
  ) {}
  
  ngOnInit(): void {
    this.userDataForm = this.fb.group({
    nombre: ['', Validators.required],
    telefono: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    direccion: ['', Validators.required],
    semester: [null, [Validators.required, Validators.min(0), Validators.max(13)]],
    });
    this.userService.getInformationUser().subscribe({
      next: (res: userResponseDTO) => {
      if (res && res.datos) {
        this.userDataForm.patchValue({
          nombre: res.datos.name ?? '',
          email: res.datos.email ?? '',
          telefono: res.datos.phone ?? '',
          direccion: res.datos.address?? '',
          semester: res.datos.semester ?? null
        });
      }
      },
    error: (err) => {
      console.error("❌ Error al obtener información del usuario:", err);
    }
    });
  }

  async onSubmit() {
    if (this.userDataForm.invalid) {
      Object.values(this.userDataForm.controls).forEach(c => c.markAsTouched());
      this.alertService.createAlert('Formulario inválido o falta imagen', 'warning', false);
    }

    this.isLoading = true;
    const formData = new FormData();
    formData.append('nombre', this.userDataForm.value.nombre);
    formData.append('telefono', this.userDataForm.value.telefono);
    formData.append('email', this.userDataForm.value.email);
    formData.append('semester', this.userDataForm.value.semester);
    formData.append('direccion', this.userDataForm.value.direccion);
    if (this.selectedFile) {
      formData.append('coverUrl', this.selectedFile);
  }
  }

   onFileSelected(event: any): void {
    const file = event.target.files[0];

    if (!file) {
      this.selectedFile = null;
      this.previewUrl = null;
      return;
    }

    if (!file.type.match(/image\/(png|jpeg)/)) {
      this.imageError = 'Solo se permiten imágenes PNG o JPEG';
      this.previewUrl = null;
      return;
    }

    if (file.size > 300 * 1024) {
      this.imageError = 'La imagen no debe superar los 300 KB';
      this.previewUrl = null;
      return;
    }

    this.imageError = null;
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result;
    reader.readAsDataURL(file);
  }

  goBack(): void {
    this.router.navigate(['/teacher']);
  }

  async urlToFile(url: string, filename: string): Promise<File> {
    const response = await fetch(url);
    const blob = await response.blob();
    return new File([blob], filename, { type: blob.type });
  }

   get f() {
    return this.userDataForm.controls;
  }

 

}
