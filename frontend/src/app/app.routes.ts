import { Routes } from '@angular/router';
import { RegisterUserComponent } from './core/modules/register-user/register-user.component';
import { HomeComponent } from './core/modules/home-layout/home/home.component';
import { LoginComponent } from './core/modules/login/login';
import { RecoverPassword } from './core/modules/recover-password/recover-password';
import { SendEmail } from './core/modules/send-code-email/send-code-email';
import { CreateCourses } from './core/modules/create-courses/create-courses';
import { CreateCategories } from './core/modules/create-categories/create-categories';
import { CreateStatusCourse } from './core/modules/create-status-course/create-status-course';
import { CreateAuditStatus } from './core/modules/create-audit-status/create-audit-status';
import { TeacherComponent } from './core/modules/teacher/teacher';

export const routes: Routes = [
  { path: 'register', component: RegisterUserComponent },
  { path: 'home', component: HomeComponent },
  { path: 'recover-password', component: RecoverPassword }, 
  { path: 'send-code-email', component: SendEmail }, 
  { path: 'recover-paassword', component: SendEmail },
  { path: 'app-create-courses', component: CreateCourses },  
  { path: 'app-create-categories', component: CreateCategories },  
  { path: 'createStatusCourse', component: CreateStatusCourse },  
  { path: 'createAuditStatus', component:CreateAuditStatus },
  { path: 'teacher', component:TeacherComponent },
  { path: '', component: LoginComponent },
  { path: '**', redirectTo: 'home', pathMatch: 'full' }
];
