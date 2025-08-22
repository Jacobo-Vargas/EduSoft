import { Routes } from '@angular/router';
import { RegisterUserComponent } from './register-user/register-user.component';

export const routes: Routes = [
  { path: 'register', component: RegisterUserComponent }, // 👈 ruta para tu formulario
  { path: '', redirectTo: 'register', pathMatch: 'full' } // opcional: redirigir al registro
];
