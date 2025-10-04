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
import { ModuleComponent } from './core/modules/module/module.component';
import { CreateModuleComponent } from './core/modules/create-module-component/create-module-component';
import { LessonComponent } from './core/modules/lesson/lesson';
import { CreateLessonComponent } from './core/modules/create-lesson-component/create-lesson-component';
import { ContentComponent } from './core/modules/content/content';
import { CreateContentComponent } from './core/modules/create-content-component/create-content-component';
import { History } from './core/modules/history/history';
import { AuthGuard } from './core/guards/AuthGuard';
import { TeacherGuard } from './core/guards/TeacherGuard';
import { AuditGuard } from './core/guards/AuditGuard';
import { Profile } from './core/modules/profile/profile';
import { StudentCourses } from './core/modules/student-courses/student-courses';
import { StudentGuard } from './core/guards/StudentGuard';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterUserComponent },
  { path: 'app-profile', component: Profile },
  { path: 'recover-password', component: RecoverPassword },
  { path: 'send-code-email', component: SendEmail },
  { path: 'app-profile', component: Profile, canActivate: [AuthGuard, StudentGuard] },
  { path: 'app-student-courses', component: StudentCourses },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'app-create-courses', component: CreateCourses, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'app-create-categories', component: CreateCategories, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'createStatusCourse', component: CreateStatusCourse, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'createAuditStatus', component: CreateAuditStatus, canActivate: [AuthGuard, AuditGuard] },
  { path: 'teacher', component: TeacherComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:courseId', component: ModuleComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:courseId/create', component: CreateModuleComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:courseId/lessons/:moduleId', component: LessonComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:courseId/lessons/:moduleId/create', component: CreateLessonComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'modules/:courseId/lessons/:moduleId/contents/:lessonId', component: ContentComponent, canActivate: [AuthGuard] },
  { path: 'modules/:courseId/lessons/:moduleId/contents/:lessonId/create', component: CreateContentComponent, canActivate: [AuthGuard, TeacherGuard] },
  { path: 'history/:courseId', component: History, canActivate: [AuthGuard, TeacherGuard] },

  { path: '**', redirectTo: 'home', pathMatch: 'full' }
];