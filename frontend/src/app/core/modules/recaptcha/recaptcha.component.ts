// src/app/components/recaptcha/recaptcha.component.ts
import { Component, EventEmitter, Output, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';

declare const grecaptcha: any;

@Component({
  selector: 'app-recaptcha',
  standalone: true,
  imports: [CommonModule],
  template: `<div id="recaptcha-container"></div>`,
})
export class RecaptchaComponent implements AfterViewInit, OnDestroy {
  @Output() tokenGenerated = new EventEmitter<string>();
  private widgetId: number | null = null;

  ngAfterViewInit(): void {
    if (typeof grecaptcha !== 'undefined') {
      this.widgetId = grecaptcha.render('recaptcha-container', {
        sitekey: '6LeQDLIrAAAAAA9hFtH2WoatCxCYEqhmraFeuGly', // <-- Reemplaza con tu Site Key de Google
        callback: (response: string) => {
          this.tokenGenerated.emit(response);
        }
      });
    }
  }

  reset(): void {
    if (this.widgetId !== null) {
      grecaptcha.reset(this.widgetId);
    }
  }

  ngOnDestroy(): void {
    this.reset();
  }
}
