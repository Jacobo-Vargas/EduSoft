import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-payment-result',
  templateUrl: './payment-result.component.html',
  standalone: false,
  styleUrls: ['./payment-result.component.css']
})
export class PaymentResultComponent implements OnInit {
  status: string | null = null;
  paymentId: string | null = null;
  preferenceId: string | null = null;
  isProcessing = false;
  courseid: string | null = null;
  message: string | null = null;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private courseService: CourseService,
    private alertService: AlertService) { }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      this.status = params.get('collection_status');
      this.paymentId = params.get('payment_id');
      this.preferenceId = params.get('preference_id');
      this.courseid = params.get('external_reference');

      if (!this.status || this.status === 'null') {
        this.status = 'failed';
        this.message = '❌ El pago no se completó o fue cancelado.';
      }


      if (this.status === 'approved' && this.preferenceId) {
        this.registerCourse();
      } else if (this.status === 'failed') {
        this.message = 'El pago no se completó o fue cancelado.';
      }
    });
  }


  registerCourse() {
    this.isProcessing = true;
    this.courseService.enrollToCourse(Number(this.courseid)).subscribe({
      next: () => {
        this.isProcessing = false;
        this.message = '✅ Te has inscrito correctamente al curso';
      },
      error: (err) => {
        this.isProcessing = false;
        this.message = err?.error || '❌ No se pudo realizar la inscripción';
      }
    });
  }

  goBack() {
    window.history.back();
  }

  goHome() {
    this.router.navigate(['/home']);
  }
}
