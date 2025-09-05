import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { LayoutModule } from "./core/layout/layout.module";
import { NavbarComponent } from './core/layout/components/navbar/navbar.component';
import { TranslateService } from '@ngx-translate/core';
import { UserInfoService } from './core/services/user-info.service';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {

  protected readonly title = signal('frontend');

  constructor(public userInfo: UserInfoService) {}

}
