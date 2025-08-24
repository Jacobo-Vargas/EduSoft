import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import Swal, { SweetAlertIcon } from 'sweetalert2';


@Injectable({
  providedIn: 'root',
})
export class AlertService {

  public jsonData: any = {};

  constructor(public translate: TranslateService, private http: HttpClient) {
    this.loadJsonData('es');
  }

  error(message: string): void {
    alert(`Error: ${message}`);
  }

  /** Creates a customized alert or confirmation dialog using the SweetAlert2 library.
   * @param text - The text content of the alert or confirmation dialog.
   * @param icon - The icon to display in the dialog (success, error, info, warning).
   * @param isConfirmation - A boolean indicating whether this is a confirmation dialog.
   * @returns A Promise representing the user's interaction with the alert.s
   */
  public async createAlert(
    text: string,
    icon: SweetAlertIcon,
    isConfirmation: boolean
  ) {
    if (isConfirmation) {
      return Swal.fire({
        text: text,
        icon: icon,
        showCancelButton: true,
        confirmButtonText: 'Aceptar',
        cancelButtonText: 'Cancelar',
      });
    } else {
      return Swal.fire({
        text: text,
        icon: icon,
        confirmButtonText: 'Aceptar',
      });
    }
  }

  /** Creates a customized alert or confirmation dialog using the SweetAlert2 library.
 * @param text - The text content of the alert or confirmation dialog.
 * @param icon - The icon to display in the dialog (success, error, info, warning).
 * @param isConfirmation - A boolean indicating whether this is a confirmation dialog.
 * @returns A Promise representing the user's interaction with the alert.s
 */
  public async createAlertHTML(
    title: string,
    text: string,
    icon: SweetAlertIcon,
    isConfirmation: boolean
  ) {
    if (isConfirmation) {
      return Swal.fire({
        title: title,
        html: text,
        icon: icon,
        showCancelButton: true,
        confirmButtonText: this.jsonData['alert']?.['btnAccept'] || 'Aceptar',
        cancelButtonText: this.jsonData['alert']?.['btnCancel'] || 'Cancelar',
      });
    } else {
      return Swal.fire({
        title: title,
        html: text,
        icon: icon,
        confirmButtonText: this.jsonData['alert']?.['btnAccept'] || 'Aceptar',
      });
    }
  }

  /**
   * Loads JSON data for a specific language and stores it in the 'jsonData' property.
   *
   * @param language - The language code or filename (without the file extension) for the desired JSON data.
   */
  public async loadJsonData(language: string) {
    try {
      this.jsonData = await this.loadJson(language);
    } catch (error) {
      //console.error('Error loading JSON:', error);
    }
  }

  /**
   * Loads a JSON file with the specified filename from the 'assets/i18n' directory.
   *
   * @param fileName - The name of the JSON file to load (without the file extension).
   * @returns A Promise that resolves with the JSON data from the file.
   */
  public loadJson(fileName: string): Promise<any> {
    const jsonUrl = `assets/i18n/${fileName}.json`;
    return this.http.get(jsonUrl).toPromise();
  }

  /** Creates a customized alert or confirmation dialog using the SweetAlert2 library.
    */
  public async createAlertLoading() {

    Swal.fire({
      titleText: 'Cargando...',
      allowOutsideClick: false,
      allowEscapeKey: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });
  }

  public closeAlertLoading() {
    return Swal.close();
  }

  public async confirmCustomAlert(
    text: string,
    icon: SweetAlertIcon,
    confirmText: string = 'Aceptar',
    cancelText: string = 'Cancelar'
  ) {
    return Swal.fire({
      text,
      icon,
      showCancelButton: true,
      confirmButtonText: confirmText,
      cancelButtonText: cancelText,
    });
  }

  public async createCustomAlert(
    text: string,
    icon: SweetAlertIcon | 'custom',
    imageUrl?: string,
    confirmText?: string
  ) {
    return Swal.fire({
      title: '',
      html: text,
      icon: icon !== 'custom' ? icon : undefined,
      imageUrl: imageUrl ? imageUrl : undefined,
      imageWidth: 60,
      imageHeight: 60,
      confirmButtonText: confirmText,
      confirmButtonColor: '#032A5A',
      showCloseButton: true,
      allowOutsideClick: false,
      customClass: {
        popup: 'swal-popup',
        confirmButton: 'swal-confirm'
      }
    });
  }

  public async createSimpleAlert(
    text: string,
    confirmButton: string,
    cancelButton: string
  ) {
    return Swal.fire({
      title: '',
      html: text,
      showCloseButton: true,
      showCancelButton: true,
      cancelButtonText: cancelButton,
      confirmButtonText: confirmButton,
      allowOutsideClick: false,
      customClass: {
        popup: 'swal-simple',
        confirmButton: 'swal-confirm',
        cancelButton: 'swal-cancel',
        closeButton: 'swal-close'
      }
    });
  }

    public async endSimpleAlert(
    text: string,
    confirmButton: string,
  ) {
    return Swal.fire({
      title: '',
      html: text,
      confirmButtonText: confirmButton,
      allowOutsideClick: false,
      customClass: {
        popup: 'swal-simple',
        confirmButton: 'swal-confirm',
        cancelButton: 'swal-cancel',
        closeButton: 'swal-close'
      }
    });
  }

showError(message: string): void {
  console.error('[Error]:', message);
  // Aquí puedes usar un toast, modal, snackbar, etc.
  alert(message); // opción temporal
}


}
