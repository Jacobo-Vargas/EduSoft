import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { provideTranslateHttpLoader, TranslateHttpLoader } from '@ngx-translate/http-loader';
import { CommonModule } from '@angular/common';
import { App } from './app';
import { routes } from './app.routes';
import { RouterModule } from '@angular/router';
import { LayoutModule } from "./core/layout/layout.module";
import { NavbarComponent } from './core/layout/components/navbar/navbar.component';
import { ModuleModule } from './core/modules/module.module';

export function httpTranslateLoader() {
  return new TranslateHttpLoader();
}

@NgModule({
  declarations: [
    App
  ],
  imports: [
    LayoutModule,
    RouterModule.forRoot(routes),
    BrowserModule,
    FormsModule,
    ModuleModule,
    HttpClientModule,
    TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: httpTranslateLoader,
            deps: [HttpClient]
        }
    }),
],
  providers: [
    provideTranslateHttpLoader(),
  ],
  bootstrap: [App]
})
export class AppModule { }
