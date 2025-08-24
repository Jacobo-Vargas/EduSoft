import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-accordion-info',
  standalone: false,
  templateUrl: './accordion-info.component.html',
  styleUrl: './accordion-info.component.css'
})
export class AccordionInfoComponent {
  @Input() title!: string;
  @Input() description!: string;

  isOpen = false;

  toggle() {
    this.isOpen = !this.isOpen;
  }
}
