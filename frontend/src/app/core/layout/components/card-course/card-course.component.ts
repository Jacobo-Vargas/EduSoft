import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../../services/alert.service';

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
  cursos: any;

  constructor(public router: Router, public alertService: AlertService) { }

  generateAction() {
    if (this.action === 1) {
      this.router.navigate(['/invest']);
    }
  }

}
