import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { userRequestDTO, userResponseDTO, UsuarioService } from '../../services/usuario.service';

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
  userRequest: userRequestDTO | null = null;
  email: string = '';

  constructor(
    private router: Router,
    private alertService: AlertService,
    public fb: FormBuilder,
    private userService: UsuarioService
  ) { }

  ngOnInit(): void {
    this.userDataForm = this.fb.group({
      nombre: ['', Validators.required],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
      direccion: ['', Validators.required],
      semester: [null, [Validators.required, Validators.min(1), Validators.max(13)]],
      coverUrl: [''],
    });
    this.userService.getInformationUser().subscribe({
      next: (res: userResponseDTO) => {
        if (res && res.datos) {
          this.email = res.datos.email ?? '';
          this.userDataForm.patchValue({
            nombre: res.datos.name ?? '',
            email: res.datos.email ?? '',
            telefono: res.datos.phone ?? '',
            direccion: res.datos.address ?? '',
            semester: res.datos.semester ?? null
          });
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

  async onSubmit() {

    if (this.userDataForm.invalid) {
      Object.values(this.userDataForm.controls).forEach(c => c.markAsTouched());
      this.alertService.createAlert(
        'Formulario inválido, se deben llenar todos los campos',
        'warning',
        false
      );
      return;
    }

    this.isLoading = true;
    const formData = new FormData();

    formData.append('documentNumber', this.userDataForm.value.documentNumber);
    formData.append('name', this.userDataForm.value.nombre);
    formData.append('phone', this.userDataForm.value.telefono);
    formData.append('email', this.email);
    formData.append('address', this.userDataForm.value.direccion);
    formData.append('password', this.userDataForm.value.password);
    formData.append('semester', this.userDataForm.value.semester);

    if (this.selectedFile) {
      formData.append('coverUrl', this.selectedFile);
    }
    this.userService.putInformationUpdateUser(formData).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.codigo === 200) {
          this.userService.getInformationUser().subscribe((res) => {
            this.userService.setUserData(res); // refresca el observable
          });
          this.alertService.createAlert('Perfil actualizado con éxito', 'success', false);
          this.router.navigate(['/dashboard']);
        } else {
          this.alertService.createAlert(res.message || 'Error al actualizar', 'error', false);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.alertService.createAlert(
          'Error en el servidor al actualizar perfil',
          'error',
          false
        );
        console.error('❌ Error backend:', err);
      }
    });
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
