import { Component, Input } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { CRUDService } from '../../../services/crud.service';

@Component({
  selector: 'app-document-viewer',
  standalone: false,
  templateUrl: './document-viewer.component.html',
  styleUrl: './document-viewer.component.css'
})
export class DocumentViewerComponent {
  @Input() pdfSrc: SafeResourceUrl = '';
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() pdfUrl: string = ''; 


  constructor(public crudService: CRUDService) { 

  }

  openViewer() {
    this.crudService.isModalOpen = true;
  }

  closeViewer() {
    document.body.style.overflow = 'auto';
    this.crudService.isModalOpen = false;
  }

  accept() {
    this.closeViewer();
  }

  downloadPDF() {
    const link = document.createElement('a');
    link.href = this.pdfUrl;
    link.download = this.pdfUrl.split('/').pop() || 'documento.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
