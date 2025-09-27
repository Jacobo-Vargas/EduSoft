import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuComponent } from './components/menu/menu.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { StagesViewComponent } from './components/stages-view/stages-view.component';
import { ProgressComponent } from './components/progress/progress.component';
import { ProgressBarComponent } from './components/progress-bar/progress-bar.component';
import { DropdownHelpComponent } from './components/dropdown-help/dropdown-help.component';
import { AccordionInfoComponent } from './components/accordion-info/accordion-info.component';
import { FormsModule } from '@angular/forms';
import { DocumentViewerComponent } from './components/document-viewer/document-viewer.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { CardCourseComponent } from './components/card-course/card-course.component';
import { httpTranslateLoader } from '../../app.module';
import { RouterModule } from '@angular/router';
import { NavbarStudent } from './components/navbar-student/navbar-student';

@NgModule({
  declarations: [
    MenuComponent,
    NavbarComponent,
    CardCourseComponent,
    StagesViewComponent,
    ProgressComponent,
    ProgressBarComponent,
    DropdownHelpComponent,
    AccordionInfoComponent,
    DocumentViewerComponent,
    NavbarStudent
  ], schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    TranslateModule.forChild()
  ],
  exports: [
    NavbarComponent,
    MenuComponent,
    CardCourseComponent,
    StagesViewComponent,
    ProgressComponent,
    DropdownHelpComponent,
    AccordionInfoComponent,
    DocumentViewerComponent,
    NavbarStudent
  ]
})
export class LayoutModule { }
