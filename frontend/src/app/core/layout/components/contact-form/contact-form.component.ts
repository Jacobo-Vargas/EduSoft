import { Component, Input } from '@angular/core';
import { CRUDService } from '../../../services/crud.service';
import { AlertService } from '../../../services/alert.service';


@Component({
  selector: 'app-contact-form',
  standalone: false,
  templateUrl: './contact-form.component.html',
  styleUrl: './contact-form.component.css'
})

export class ContactFormComponent {

  @Input() crudService!: CRUDService;
  constructor (public alertService: AlertService) { }
title=''
question=''


  async onSubmit(event: Event) {
    event.preventDefault();
    if (this.title && this.question) {
      if (!this.crudService.fineractId) {
        this.alertService.createAlertHTML('','Desbes hacer onboarding para poder contactarte','info',false )
        return;
      }
      this.crudService.dataFormAux.userId = this.crudService.fineractId;
    const response= await this.crudService.getHttp('/generate-question', {...this.crudService.dataFormAux,title: this.title, question: this.question});
      this.alertService.createAlertHTML('',response.message,'success',false )
      this.title=''
      this.question=''
    } else {
      this.alertService.createAlert("Por favor ingrese un título y una descripción", "warning", false);
    }
  }
}
