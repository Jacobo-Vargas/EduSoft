import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { httpTranslateLoader } from '../../app.module';
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
    SendEmail

  ], schemas: [CUSTOM_ELEMENTS_SCHEMA],
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
    SendEmail
  ]
})
export class MooduleModule { }
