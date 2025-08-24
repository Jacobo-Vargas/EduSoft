import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../../services/alert.service';
import { UserInfoService } from '../../../services/user-info.service';

@Component({
  selector: 'app-card-course',
  standalone: false,
  templateUrl: './card-course.component.html',
  styleUrl: './card-course.component.css'
})
export class CardCourseComponent {
  @Input() bgColor!: string;
  @Input() imageSrc!: string;
  @Input() title!: string;
  @Input() description!: string;
  @Input() buttonText!: string;
  @Input() iconColor!: string;
  @Input() action!: number;

  constructor(public router: Router, public alertService: AlertService,  public userInfoService: UserInfoService) { }

    generateAction() {
    if (this.action === 1) {
      this.router.navigate(['/invest']);
    } else if (this.action === 2) {
      this.handleConditionalRedirect('/sigma-fund');
    } else {
      this.handleConditionalRedirect('/capital-investment');
    }
  }

  handleConditionalRedirect(route: string) {
    if (this.userInfoService.country === 'Chile') {
      this.router.navigate([route]);
    } else {
      this.alertService.createSimpleAlert(
        'Este instrumento está domiciliado en Chile ¿Tienes un Rol Único Tributario de Chile?',
        'Si, quiero saber más',
        'No, buscaré otros productos',
      ).then(result => {
        if (result.value) {
          this.router.navigate([route]);
        }
      });
    }

  // generateAction() {
  //   if (this.action == 1) {
  //     this.router.navigate(['/invest']);
  //   } else if (this.action == 2) {
  //     this.redirectFund('/sigma-fund');
  //   } else {
  //     this.redirectFund('/capital-investment');
  //   }
  // }

  // redirectFund(route: string) {
  //   this.alertService.createSimpleAlert(
  //     'Este instrumento está domiciliado en Chile ¿Tienes un Rol Único Tributario de Chile?',
  //     'Si, quiero saber más',
  //     'No, buscaré otros productos',
  //   ).then(result => {
  //     if (result.value) {
  //       this.router.navigate([route]);
  //     }
  //   });
  }
}
