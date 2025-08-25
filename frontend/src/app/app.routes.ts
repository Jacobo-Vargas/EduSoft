import { Routes } from '@angular/router';
import { RegisterUserComponent } from './core/modules/register-user/register-user.component';
import { HomeComponent } from './core/modules/home-layout/home/home.component';
import { LoginComponent } from './core/modules/login/login';

export const routes: Routes = [
  { path: 'register', component: RegisterUserComponent },
  { path: 'login', component: LoginComponent },
  { path: '', component: HomeComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];
