import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';
import { CreateCourses } from './create-courses/create-courses';
import { CreateAuditStatus } from './create-audit-status/create-audit-status';
import { CreateCategories } from './create-categories/create-categories';
import { CreateStatusCourse } from './create-status-course/create-status-course';
import { LoginComponent } from './login/login';
import { RecaptchaComponent } from './recaptcha/recaptcha.component';
import { RecoverPassword } from './recover-password/recover-password';
import { RegisterUserComponent } from './register-user/register-user.component';
import { SendEmail } from './send-code-email/send-code-email';
import { TeacherComponent } from './teacher/teacher';
import { CreateModuleComponent } from './create-module-component/create-module-component';
import { ModuleComponent } from './module/module.component';
import { LessonComponent } from './lesson/lesson';
import { CreateLessonComponent } from './create-lesson-component/create-lesson-component';
import { History } from './history/history';
import { ContentComponent } from './content/content';
import { CreateContentComponent } from './create-content-component/create-content-component';
import { Profile } from './profile/profile';
import { StudentCourses } from './student-courses/student-courses';
import { ViewCourseComponent } from './view-course/view-course';
import { SafeUrlPipe } from '../pipes/safe-url.pipe';



@NgModule({
  declarations: [
    CreateCourses,
    CreateAuditStatus,
    CreateCategories,
    CreateStatusCourse,
    LoginComponent,
    RecaptchaComponent,
    RecoverPassword,
    RegisterUserComponent,
    SendEmail,
    TeacherComponent,
    CreateModuleComponent,
    ModuleComponent,
    LessonComponent,
    CreateLessonComponent,
    History,
    ContentComponent,
    CreateContentComponent,
    Profile,
    StudentCourses,
    SafeUrlPipe,
    ViewCourseComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    FormsModule,
    RouterModule,
    TranslateModule.forChild()
  ],
  exports: [
    CreateCourses,
    CreateAuditStatus,
    CreateCategories,
    CreateStatusCourse,
    LoginComponent,
    RecaptchaComponent,
    RecoverPassword,
    RegisterUserComponent,
    SendEmail,
    TeacherComponent,
    CreateModuleComponent,
    ModuleComponent,
    LessonComponent,
    CreateLessonComponent,
    History,
    ContentComponent,
    CreateContentComponent,
    Profile,
    StudentCourses,
    SafeUrlPipe,
    ViewCourseComponent
  ]
})
export class MooduleModule { }
