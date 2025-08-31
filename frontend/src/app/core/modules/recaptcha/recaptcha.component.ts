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
        sitekey: '6Lff4LgrAAAAAKVFhq1YtlwOImtIeE23mMD9e6ZL',
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
