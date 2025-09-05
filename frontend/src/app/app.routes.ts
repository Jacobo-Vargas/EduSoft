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
import { TeacherGuard } from './core/guards/teacher.guard';
import { ModuleComponent } from './core/modules/module/module.component';
import { CreateModuleComponent } from './core/modules/create-module-component/create-module-component';
import { LessonComponent } from './core/modules/lesson/lesson';

export const routes: Routes = [
  { path: 'register', component: RegisterUserComponent },
  { path: 'home', component: HomeComponent },
  { path: 'recover-password', component: RecoverPassword },
  { path: 'send-code-email', component: SendEmail },
  { path: 'app-create-courses', component: CreateCourses },
  { path: 'app-create-categories', component: CreateCategories },
  { path: 'createStatusCourse', component: CreateStatusCourse },
  { path: 'createAuditStatus', component: CreateAuditStatus },
  { path: 'teacher', component: TeacherComponent, canActivate: [TeacherGuard] },
  { path: 'modules/:moduleId/lessons', component: LessonComponent },
  { path: 'modules/:courseId/create', component: CreateModuleComponent },
  { path: 'modules/:courseId', component: ModuleComponent },
  { path: '', component: LoginComponent },
  { path: '**', redirectTo: 'home', pathMatch: 'full' }
];
