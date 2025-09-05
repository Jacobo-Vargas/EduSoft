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
import { TeacherGuard } from './core/guards/TeacherGuard';
import { ModuleComponent } from './core/modules/module/module.component';
import { CreateModuleComponent } from './core/modules/create-module-component/create-module-component';
import { LessonComponent } from './core/modules/lesson/lesson';
import { CreateLessonComponent } from './core/modules//create-lesson-component/create-lesson-component';
import { AuditGuard } from './core/guards/AuditGuard';
import { AuthGuard } from './core/guards/AuthGuard';
import { StudentGuard } from './core/guards/StudentGuard';

export const routes: Routes = [
  { path: 'register', component: RegisterUserComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'recover-password', component: RecoverPassword },
  { path: 'send-code-email', component: SendEmail },
  { path: 'app-create-courses', component: CreateCourses, canActivate: [AuthGuard, TeacherGuard]},
  { path: 'app-create-categories', component: CreateCategories, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'createStatusCourse', component: CreateStatusCourse, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'createAuditStatus', component: CreateAuditStatus, canActivate: [AuthGuard, AuditGuard] },
  { path: 'teacher', component: TeacherComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:moduleId/lessons', component: LessonComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:moduleId/lessons/create', component: CreateLessonComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:courseId', component: ModuleComponent, canActivate: [AuthGuard, TeacherGuard]},
  { path: 'modules/:courseId/create', component: CreateModuleComponent, canActivate: [AuthGuard,TeacherGuard] },
  { path: '', component: LoginComponent},
  { path: '**', redirectTo: 'home', pathMatch: 'full' }
];
