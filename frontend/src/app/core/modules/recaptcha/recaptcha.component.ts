import { AfterViewInit, Component, EventEmitter, Output } from '@angular/core';

declare global {
  interface Window {
    grecaptcha: any;
  }
}

@Component({
  selector: 'app-recaptcha',
  template: `<div [id]="containerId"></div>`,
  standalone: false
})
export class RecaptchaComponent implements AfterViewInit {
  @Output() tokenGenerated = new EventEmitter<string>();
  private widgetId?: number;
  containerId = 'recaptcha-container-' + Math.random().toString(36).substring(2, 9);

  ngAfterViewInit(): void {
    const interval = setInterval(() => {
      if (window.grecaptcha) {
        clearInterval(interval);

        this.widgetId = window.grecaptcha.render(this.containerId, {
          sitekey: '6LcyD7srAAAAAFmDouQsxOqPXlbgA_jrbe7sXEEB',
          callback: (response: string) => this.tokenGenerated.emit(response),
          'expired-callback': () => this.tokenGenerated.emit(''),
          'error-callback': () => this.tokenGenerated.emit('')
        });
      }
    }, 300);
  }

  reset(): void {
    if (this.widgetId !== undefined && window.grecaptcha) {
      window.grecaptcha.reset(this.widgetId);


    }
  }
}
