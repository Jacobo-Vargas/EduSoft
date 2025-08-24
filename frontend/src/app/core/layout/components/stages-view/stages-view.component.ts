import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stages-view',
  standalone: false,
  templateUrl: './stages-view.component.html',
  styleUrl: './stages-view.component.css'
})
export class StagesViewComponent {
  @Input() stageActive!:boolean;
}
