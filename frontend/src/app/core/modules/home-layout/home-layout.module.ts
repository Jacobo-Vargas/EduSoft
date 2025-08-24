import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { LayoutModule } from '../../layout/layout.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    HomeComponent
  ], schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    CommonModule,
    LayoutModule,
    RouterModule
  ],
  providers: [TitleCasePipe],
  exports: [
    HomeComponent
  ]
})
export class HomeLayoutModule { }
