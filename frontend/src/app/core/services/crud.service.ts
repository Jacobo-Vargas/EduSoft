import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject, catchError, firstValueFrom, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AlertService } from './alert.service';
import { UserInfoService } from './user-info.service';

declare var $: any;

@Injectable({
  providedIn: 'root',
})
//CRUDService
export class CRUDService {
  //Menu
  sidebarOpen: boolean = false;

  //Modal
  private showModal = new BehaviorSubject<boolean>(false);
  showModal$ = this.showModal.asObservable();

  //Table data
  public dataList!: any[];
  public projects!: any[];
  public projectDetails: any = {
    occupancyPercentage: 0,
    confirmedAmount: 0,
    amount: 0
  };
  public schedule: any = {};

  // Método para calcular y actualizar el porcentaje de ocupación
  private updateOccupancyPercentage() {
    if (this.projectDetails.amount && this.projectDetails.confirmedAmount) {
      this.projectDetails.occupancyPercentage = Math.min(
        Math.round((this.projectDetails.confirmedAmount / this.projectDetails.amount) * 100),
        100
      );
    } else {
      this.projectDetails.occupancyPercentage = 0;
    }
  }

  // Método para actualizar los detalles del proyecto
  public updateProjectDetails(details: any) {
    this.projectDetails = {
      ...details,
      confirmedAmount: details.confirmedAmount || 0,
      amount: details.amount || 0
    };
    this.updateOccupancyPercentage();
  }

  public investorProfile: any = {};
  public amountFee!: number;

  //URL of the pager pages
  public urlPage!: any;

  //Page containing data
  public page!: any;

  //File path
  public pathFile!: string;

  //Data to show in the view
  public dataShow: any = {};

  //Form data to send to the controller
  public dataForm: any = {};

  //Flags to show or hide modal show, modal form and filters
  public isShowOpen = false;
  public isFormOpen = false;
  public filterShow = false;
  public buttonShow = true;
  public isModalOpen = false;

  //Lista de items in entity
  public dataFormAux: any = {};
  public dataFormTemp: any = {};
  public dataShowAux: any = {};
  public secondDataShowAux: any = {};

  //Filter data
  public dataFilter: any = {};

  //list of selected items
  public dataItems: any = {};

  constructor(
    private http: HttpClient,
    private userInfoService: UserInfoService,
    private alertService: AlertService,
    public translate: TranslateService,
    public router: Router
  ) {
    this.alertService.loadJsonData('es');
  }

  openModal() {
    this.showModal.next(true);
  }

  closeModal() {
    this.showModal.next(false);
  }

  /**
   * Fetches a list of records from the specified API endpoint.
   * @param endPoint - The URL of the API endpoint to retrieve data from.
   * @returns A promise that is resolved when the data is successfully recovered.
   */
  public async getAll(endPoint: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Create a Loading alert to indicate that a process is loading
    await this.alertService.createAlertLoading();

    // Retrieve the authentication token from the user info service.
    const token = await this.userInfoService.getToken();

    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    await this.http
      .get<any>(environment.apiUrl + endPoint, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        this.page = data.page;
        this.dataList = this.page.content;
        this.urlPage = data.url;
        this.page.pages = data.pages;
        this.page.currentPage = data.currentPage;
        this.page.hasPrevius = data.hasPrevius;
        this.page.hasNext = data.hasNext;

        // Close alerts
        this.alertService.closeAlertLoading();
      });
  }

  /**
   * Fetches a list of records from the specified API endpoint.
   * @param endPoint - The URL of the API endpoint to retrieve data from.
   * @returns A promise that is resolved when the data is successfully recovered.
   */
  public async getAllProjects(endPoint: string): Promise<any> {
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();

    const headers = {
      Authorization: token,
      country: this.userInfoService.country,
    };

    try {
      const data = await firstValueFrom(
        this.http
          .get<any>(environment.apiUrl2 + endPoint, {headers})
          .pipe(catchError((error) => {
            this.userInfoService.handleError(error);
            return throwError(() => error);
          }))
      );

      this.projects = data;
      this.alertService.closeAlertLoading();
      return this.projects;
    } catch (error) {
      this.alertService.closeAlertLoading();
      console.error("Error al cargar proyectos: ", error);
      throw error;
    }
  }

  /**
   * Fetches a list of records from the specified API endpoint.
   * @param endPoint - The URL of the API endpoint to retrieve data from.
   * @returns A promise that is resolved when the data is successfully recovered.
   */
  public async getList(endPoint: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Create a Loading alert to indicate that a process is loading
    await this.alertService.createAlertLoading();
    // Retrieve the authentication token from the user info service.
    const token = await this.userInfoService.getToken();

    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
    };

    await this.http
      .get<any>(environment.apiUrl + endPoint, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        this.dataList = data;
        this.loadCheckBoxTime();

        this.alertService.closeAlertLoading();
      });
  }

  /**
   * Fetches a list of records from the specified API endpoint.
   * @param endPoint - The URL of the API endpoint to retrieve data from.
   * @returns A promise that is resolved when the data is successfully recovered.
   */
  public async getListData(endPoint: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Create a Loading alert to indicate that a process is loading
    await this.alertService.createAlertLoading();
    // Retrieve the authentication token from the user info service.
    const token = await this.userInfoService.getToken();

    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
    };

    await this.http
      .get<any>(environment.apiUrl + endPoint, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        this.dataList = data.data;
        this.loadCheckBoxTime();

        this.alertService.closeAlertLoading();
      });
  }

  /**
   * Updates a specific record by its ID via a PUT request.
   *
   * @param endPoint - The URL of the API endpoint.
   * @param id - The ID of the record to updated.
   * @param modalName - The name of the modal associated with this update operation.
   * @returns A Promise containing the updated record or an error message.
   */
  public async update(
    endPoint: string,
    id: any,
    modalName: string,
    dataForm: any
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Create a Loading alert to indicate that a process is loading
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    const url = environment.apiUrl + endPoint + '/' + id;
    let data = await this.makeFormData(dataForm);
    this.http
      .put<any>(url, data, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        if (data.type_message == 'success') {
          this.alertService.createAlert(data.message, data.type_message, false);
          this.getAll(this.urlPage);
          this.closeForm('#modalForm' + modalName);
        } else {
          this.alertService.createAlert(data.message, data.type_message, false);
        }
      });
  }

  public async create(
    endPoint: string,
    route: string,
    dataForm: any,
    isOnboarding: boolean
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
      'Content-Type': 'application/json',
    };

    if ((endPoint.includes('factory-initial-phase') || isOnboarding) &&
      dataForm.firstName && dataForm.lastname && dataForm.documentNumber) {
      const firstInitial = dataForm.firstName.substring(0, 2).toUpperCase();
      const lastInitial = dataForm.lastname.substring(0, 2).toUpperCase();
      const docEnd = String(dataForm.documentNumber).slice(-4);
      const timestamp = new Date().getTime().toString().slice(-4);
      dataForm.mnemonic = `${firstInitial}${lastInitial}${docEnd}${timestamp}`;
    }
    console.log(JSON.stringify(dataForm))
    return new Promise((resolve, reject) => {
      this.http.post<any>(environment.apiUrl + endPoint, JSON.stringify(dataForm), {headers})
        .pipe(
          catchError((error) => {
            this.alertService.closeAlertLoading();
            const msg = error?.error?.message || error?.message || 'Error desconocido';
            this.alertService.createAlert(msg, 'error', false);
            reject(error);
            return throwError(() => error);
          })
        )
        .subscribe({
          next: async (response) => {
            this.alertService.closeAlertLoading();
            if (response.success === true && response.type_message !== 'error') {
              await this.router.navigate(['/end-registration']);
            } else {
              await this.alertService.endSimpleAlert(
                'Ha ocurrido un error con el registro. Verifica todas las respuestas del formulario.',
                'Salir',
              );
              await this.router.navigate(['/']);
              return reject('Registro existente u otro error lógico');
            }
          },
          error: async (err: any) => {
            console.warn('Error en la creación:', err);
            this.alertService.closeAlertLoading();
            await this.alertService.endSimpleAlert(
              'Ha ocurrido un error con el registro. Verifica todas las respuestas del formulario.',
              'Salir',
            );
            await this.router.navigate(['/']);
            return reject('Error inesperado');
          }
        });
    });
  }


  public async getHttp(
    endPoint: string,
    dataForm: any,
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    const data = await this.makeFormData(dataForm);

    try {
      const response = await firstValueFrom(
        this.http.post<any>(environment.apiUrl + endPoint, data, {headers}).pipe(
          catchError(error => {
            const msg = error?.error?.message || error?.message || 'Error desconocido';
            this.alertService.createAlert(msg, 'error', false);
            throw error;
          })
        )
      );

      this.alertService.closeAlertLoading();

      if (response.success === true && response.type_message !== 'error') {
        return response;
      } else {
        await this.alertService.endSimpleAlert('Ha ocurrido un error.', 'Salir');
        throw new Error('Respuesta con error');
      }
    } catch (error) {
      this.alertService.closeAlertLoading();
      await this.alertService.endSimpleAlert('Ha ocurrido un error', 'Salir');
      throw error;
    }
  }


  /**
   * Fetches a list of records from the specified API endpoint.
   * @param endPoint - The URL of the API endpoint to retrieve data from.
   * @returns A promise that is resolved when the data is successfully recovered.
   */
  public async getAmountFee(endPoint: string): Promise<void> {
    //await this.alertService.loadJsonData(this.translate.currentLang);

    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();

    const headers = {
      //'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    await this.http
      .get<any>(environment.apiUrl2 + endPoint, {headers})
      .pipe(catchError((error) => this.userInfoService.handleError(error)))
      .subscribe((data) => {
        this.amountFee = data;
        this.alertService.closeAlertLoading();
      });
  }

  /**
   * Creates a new record via a POST request.
   *
   * @param endPoint - The URL of the API endpoint.
   * @param modalName -  The name of the modal associated with this create operation
   * @returns A Promise containing the updated record or an error message.
   */
  public async generateInvestment(
    endPoint: string,
    dataForm: any
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    // Create a Loading alert to indicate that a process is loading
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    let data = await this.makeFormData(dataForm);

    this.http
      .post<any>(environment.apiUrl2 + endPoint, data, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        //console.log(data);
        if (data.resourceId) {
          this.alertService
            .createAlert('Se realizó su inversión', 'success', false)
            .then((result) => {
              if (result) {
                this.dataFormAux = {};
                this.schedule = {};
              }
            });
        } else {
          this.alertService.createAlert(data.message, data.type_message, false);
        }
      });
  }

  /**
   * Deletes a record by its ID via a DELETE request.
   *
   * @param endPoint - The URL of the API endpoint.
   * @param id - The ID of the record to be deleted.
   * @returns A Promise indicating success or failure.
   */
  public async deleteById(endPoint: string, id: any): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
    };
    // create endpoint
    const url = environment.apiUrl + endPoint + '/' + id;

    // Check the decision
    this.alertService
      .createAlert(
        this.alertService.jsonData['alert']['deleteMessage'],
        'warning',
        true
      )
      .then((result) => {
        if (result.value) {
          this.http
            .delete<any>(url, {headers})
            .pipe(
              //Catch the error to generate a new action
              catchError((error) => this.userInfoService.handleError(error))
            )
            .subscribe((data) => {
              if (data.type_message == 'success') {
                this.alertService
                  .createAlert(data.message, data.type_message, false)
                  .then((result) => {
                    if (result.value) {
                      this.getAll(this.urlPage);
                    }
                  });
              } else {
                this.alertService.createAlert(
                  data.message,
                  data.type_message,
                  false
                );
              }
            });
        }
      });
  }

  /**
   * Retrieves a specific record by its ID via a GET request.
   *
   * @param endPoint - The URL of the API endpoint.
   * @param id - The ID of the record to be retrieved.
   * @returns A Promise that resolves with the retrieved record.
   */  public async getById(endPoint: string, id: any): Promise<any> {
    await this.alertService.createAlertLoading();
    const token = await this.userInfoService.getToken();
    const headers = {
      // 'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };
    const url = environment.apiUrl2 + endPoint + '/' + id;

    try {
      const data = await firstValueFrom(
        this.http.get<any>(url, {headers})
      );
      if (data) {
        this.projectDetails = Object.assign({}, data);
        this.dataFormAux.tipoCliente =
          this.userInfoService.tipoCliente ||
          this.userInfoService.userInfo?.tipoCliente ||
          'retail';
      } else {
        console.log('getById - No se recibieron datos del servidor');
      }
      this.alertService.closeAlertLoading();
      return this.projectDetails;
    } catch (error) {
      this.alertService.closeAlertLoading();
      throw error;
    }
  }

  /**
   * Consulta a la API para obtener minAmount y maxAmount de un proyecto específico.
   * @param endPoint - Endpoint base
   * @param id - ID del proyecto
   * @returns Promise<{minAmount: number, maxAmount: number} | null>
   */
  private async fetchMinMaxAmountFromApi(endPoint: string, id: any): Promise<{
    minAmount: number,
    maxAmount: number
  } | null> {
    const token = await this.userInfoService.getToken();
    const headers = {
      Authorization: token,
      country: this.userInfoService.country,
    };
    // Suponiendo que existe un endpoint específico para min/max, por ejemplo: /min-max-amount/{id}
    const url = environment.apiUrl2 + endPoint + '/' + id + '/min-max-amount';
    try {
      const result = await firstValueFrom(
        this.http.get<any>(url, {headers})
      );
      if (result && result.minAmount != null && result.maxAmount != null) {
        return {minAmount: result.minAmount, maxAmount: result.maxAmount};
      }
      return null;
    } catch (error) {
      console.error('Error al obtener minAmount/maxAmount:', error);
      return null;
    }
  }

  /**
   * Sets the 'dataList' property with the specified data.
   * @param data - The data to be assigned to 'dataList'.
   */
  public setDataList(data: any) {
    this.dataList = data;
  }

  public clearDataFilter(endPoint: string) {
    this.dataFilter = {};
    this.buttonShow = true;
    this.getAll(endPoint);
  }

  public getDataFilter() {
    return this.dataFilter;
  }

  // beggin modal-show
  /**
   * Opens a modal and assigns data for display.
   * @param data - The data to be displayed in the modal.
   */
  public openShow(data: any) {
    this.dataShow = data;
  }

  /**
   * Closes the modal for displaying data.
   */
  public closeShow() {
    this.isShowOpen = false;
    this.dataShow = null;
  }

  /**
   * Retrieves the data currently set for display in the modal.
   * @returns The data to be displayed in the modal.
   */
  public getShowData() {
    return this.dataShow;
  }

  /**
   * Retrieves the data currently set for display in the modal.
   * @returns The data to be displayed in the modal.
   */
  public getShowDataAux() {
    return this.dataShowAux;
  }

  public getShowSecondDataAux() {
    return this.secondDataShowAux;
  }

  /**
   * Checks if the modal for displaying data is currently visible.
   * @returns A boolean indicating whether the modal is visible.
   */
  public isShowVisible() {
    return this.isShowOpen;
  }

  // end modal-show

  // beggin modal-form
  /**
   * Opens a form and assigns the provided data to 'dataForm' if data is provided.
   * @param data - The data to be assigned to 'dataForm'
   */
  public openForm(data: any) {
    if (data) {
      const dataClone = Object.assign({}, data);
      this.dataForm = dataClone;
    }
  }

  /**
   * Close the modal form, clears form data, and updates the visibility flag.
   * @param modalName - Name of the modal to be closed.
   */
  public closeForm(modalName: string) {
    $(modalName).modal('toggle');
    this.isFormOpen = false;
    this.clearDataForm();
  }

  public closeFormView(modalName: string) {
    $(modalName).modal('toggle');
    this.isFormOpen = false;
    this.dataFormTemp = null;
  }

  /**
   * Retrieves the form data currently assigned to 'dataForm'.
   * @returns The form data.
   */
  public getFormData() {
    return this.dataForm;
  }

  /**
   * Checks if the form is currently visible.
   * @returns A boolean indicating whether the form is visible.
   */
  public isFormVisible() {
    return this.isFormOpen;
  }

  /**
   * Clears the form data by resetting 'dataForm' to an empty object and clearing file input values.
   */
  public clearDataForm(): void {
    this.dataForm = {};
    this.dataFormAux = {};
    $('input[type=file]').val(null);
  }

  // end modal-form

  /**
   * Displays a confirmation dialog for editing and closes a modal upon confirmation.
   *
   * @param modalName - The name of the modal associated with the edit action.
   * @returns A Promise that resolves when the modal is closed or rejected if the user cancels.
   */
  public async alertEdit(modalName: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    this.alertService
      .createAlert(
        this.alertService.jsonData['alert']['editMessage'],
        'warning',
        true
      )
      .then((result) => {
        if (result.value) {
          this.closeForm('#modalForm' + modalName);
        }
      });
  }

  /**
   * Handles the selection of a file in an input element.
   *
   * @param event - The event object representing the file selection.
   * @param fileName - The name of the property in `dataForm` where the selected file should be assigned.
   */
  public onFileSelected(event: any, fileName: string) {
    const selectedFile = <File>event.target.files[0];
    if (selectedFile.size < 1024 * 1024 * 5) {
      this.dataForm[fileName] = selectedFile;
    } else {
      this.alertService.createAlert(
        this.alertService.jsonData['alert']['sizeFile'] + '5MB',
        'warning',
        false
      );
    }
  }

  /**
   * Clears the selected file in an input element associated with a specific property in dataForm.
   *
   * @param fileName - The name of the property in dataForm representing the file to be cleared.
   */
  public async clearInputFile(fileName: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    this.alertService
      .createAlert(
        this.alertService.jsonData['alert']['confirmationDelete'],
        'error',
        true
      )
      .then((result) => {
        if (result.value) {
          this.dataFormAux[fileName] = null;
        }
      });
  }

  /**
   * Creates a FormData object from the data in `dataForm`, suitable for sending in a multipart/form-data HTTP request.
   *
   * @returns A Promise that resolves with a FormData object.
   */
  public async makeFormData(dataForm: any): Promise<FormData> {
    let formData = new FormData();

    // Scroll through the form data
    for (const key in dataForm) {
      if (dataForm.hasOwnProperty(key)) {
        const data = dataForm[key];
        // Validates if it is not an object and if it is a file.
        if (
          (typeof data != 'object' ||
            data instanceof Date ||
            data instanceof Array) &&
          data != null
        ) {
          // Validates if it is an array
          if (Array.isArray(data)) {
            data.forEach((element) => {
              if (typeof element == 'object') {
                //console.log('Serialized:', JSON.stringify(element));
                formData.append(`${key}[]`, JSON.stringify(element));
              } else {
                formData.append(`${key}[]`, element);
              }
            });
          }
          // validates if it is a date
          else if (data instanceof Date) {
            // formData.append(key, locale.format(data, "YYYY-MM-DD hh:mm:ss"));
          }
          // validates if it is simple data
          else {
            formData.append(key, data);
          }
        } // validates if it is a file
        else if (data instanceof File) {
          if (data.size < 1024 * 1024 * 5) {
            formData.append(key, data as File);
          } else {
            this.alertService.createAlert(
              this.alertService.jsonData['alert']['sizeFile'] + '5MB',
              'warning',
              false
            );
          }
        } // validates if it is a object
        else if (data instanceof Object) {
          formData.append(`${key}[]`, JSON.stringify(data));
        }
      }
    }

    return formData;
  }

  /**
   * Initiates the download of a file located at the specified path by creating a temporary link and triggering a click event.
   *
   * @param path - The path to the file to be downloaded.
   */
  public downloadFile(path: string) {
    const url = '/download/file?filePath=' + path;
    const link = document.createElement('a');
    link.href = url;
    link.target = '_blank';
    link.click();
    //console.log(path);
  }

  /**
   * Sets the 'pathFile' property with a specified file path and displays a modal with the given name.
   *
   * @param path - The path to the file to be associated with 'pathFile'.
   * @param modalName - The name of the modal to be displayed.
   */
  public setPathFile(path: string, modalName: string) {
    this.pathFile = path;
    $('#' + modalName).modal('show');
  }

  public getFormDetailsModal(modalName: string, data: any) {
    if (data) {
      this.dataFormTemp = Object.assign({}, data);
      if (!this.dataFormTemp.biometric) {
        this.dataFormTemp.biometric = {};
      }
      this.dataFormTemp = Object.assign({}, data);
    }

    //console.error("modal name: "+modalName)

    $('#' + modalName).modal('show');
  }

  /**
   * Closes the file view modal with the specified name using jQuery (assuming jQuery is available).
   *
   * @param modalName - The name of the modal to be closed.
   */
  public closeFileView(modalName: string) {
    this.dataShowAux = null;
    $(modalName).modal('toggle');
  }

  public closeSecondFileView(modalName: string) {
    this.secondDataShowAux = null;
    $(modalName).modal('toggle');
  }

  public getShowDetailsModal(modalName: string, data: any) {
    if (data) {
      this.dataShowAux = Object.assign({}, data);
      if (!this.dataShowAux.biometric) {
        this.dataShowAux.biometric = {};
      }
      this.dataShowAux = data;
    }
    $('#' + modalName).modal('show');
  }

  public getShowSecondDetailsModal(modalName: string, data: any) {
    if (data) {
      this.secondDataShowAux = Object.assign({}, data);
      if (!this.secondDataShowAux.biometric) {
        this.secondDataShowAux.biometric = {};
      }
      this.secondDataShowAux = data;
    }
    $('#' + modalName).modal('show');
  }

  /**
   * Saves or deletes the id of an item when the checkbox is selected or deselected
   * @param listName - Name of the list where id's will be stored
   * @param id - Item to add or delete from the list
   */
  public isItemSelect(listName: string, id: any) {
    let indice = this.dataItems[listName].indexOf(id);
    if (indice !== -1) {
      // Removes 1 element from the index found
      this.dataItems[listName].splice(indice, 1);
    } else {
      this.dataItems[listName].push(id);
    }
  }

  /**
   * loadCheckBoxTime
   */
  public async loadCheckBoxTime() {
    let tryAgain = 0;

    const checkDataTable = () => {
      let flag = false;

      //Scrolls through the table data
      for (const item in this.dataList) {
        for (const key in this.dataItems) {
          //Check the list is not empty
          if (!this.dataItems[key].isEmpty) {
            //Scrolls through the integer list data
            for (const id in this.dataItems[key]) {
              const value = this.dataItems[key][id];

              // compares list id with table id
              // if they match, the element must be checked,
              // not checked sets the flag for flag
              if (this.dataList[item].id == value) {
                if (!$('#' + value).prop('checked')) {
                  //If a checkbox is not checked, set the flag to true.
                  flag = true;
                  // It is not necessary to check more boxes.
                  break;
                }
              }
            }
          }
        }
      }

      // Check flag
      if (flag) {
        // Trigger the method that checks the boxes.
        this.loadCheckbox();
      } else if (tryAgain < 5) {
        // Trigger the method that checks the boxes.
        this.loadCheckbox();
        tryAgain++;
      } else {
        //Exit the method.
        return;
      }
      //Triggers the check event
      setTimeout(checkDataTable, 30);
    };
    //Triggers the check event
    checkDataTable();
  }

  /**
   * Check the boxes of the products that are stored in the list.
   */
  public async loadCheckbox(): Promise<void> {
    for (const key in this.dataItems) {
      if (!this.dataItems[key].isEmpty) {
        for (const id in this.dataItems[key]) {
          const value = this.dataItems[key][id];
          $('#' + value).prop('checked', false);
          $('#' + value)
            .prop('checked', true)
            .attr('checked', 'checked');
        }
      }
    }
  }

  /**
   * Saves the lists of the dataItems in the dataForm to send to the controller.
   */
  public makeList() {
    for (const key in this.dataItems) {
      if (!this.dataItems[key].isEmpty) {
        this.dataForm[key] = this.dataItems[key];
        //Validates if the element is an array and clears it.
        if (Array.isArray(this.dataItems[key])) {
          this.dataItems[key] = [];
        } else {
          //If it is a different object, it equals null.
          this.dataItems[key] = null;
        }
      }
    }
  }

  /**
   * Check all the records that are on the table page.
   */
  public selectAll(listName: string) {
    for (const item in this.dataList) {
      let id = this.dataList[item].id;
      let indice = this.dataItems[listName].indexOf(id);
      if (indice == -1) {
        this.dataItems[listName].push(id);
        $('#' + id)
          .prop('checked', true)
          .attr('checked', 'checked');
      }
    }
    //console.log(this.dataItems[listName]);
  }

  /**
   * Unchecks all records found on the table page.
   */
  public unselectAll(listName: string) {
    for (const item in this.dataList) {
      let id = this.dataList[item].id;
      let indice = this.dataItems[listName].indexOf(id);
      if (indice !== -1) {
        // Removes 1 element from the index found
        this.dataItems[listName].splice(indice, 1);
        $('#' + id).prop('checked', false);
      }
    }
    //console.log(this.dataItems[listName]);
  }

  /**
   * Show or hide filters depending on the flag.
   */
  filterVisibility() {
    if (!this.filterShow) {
      this.filterShow = true;
    } else {
      this.filterShow = false;
    }
  }

  /*

    */
  public filterResults() {
    this.buttonShow = this.isDataFilterEmpty();
  }

  /*
      Validates that the variable dataFilter, which contains the filter values, is not empty.
    */
  private isDataFilterEmpty(): boolean {
    for (const key in this.dataFilter) {
      if (
        this.dataFilter.hasOwnProperty(key) &&
        this.dataFilter[key] !== null &&
        this.dataFilter[key] !== ''
      ) {
        return false;
      }
    }
    return true;
  }

  /*
      The route is generated according to the filters to be applied to load the information.
    */
  public getURLSearch(endPoint: string) {
    var flag = true;
    for (const key in this.dataFilter) {
      if (this.dataFilter.hasOwnProperty(key)) {
        if (this.dataFilter[key] && this.dataFilter[key] !== '') {
          if (flag) {
            endPoint += '?' + key + '=' + this.dataFilter[key];
            flag = false;
          } else {
            endPoint += '&' + key + '=' + this.dataFilter[key];
          }
        }
      }
    }
    this.getAll(endPoint);
  }

  /*
   * is responsible for storing () the values ​​of a subform, to later add it to a list of the dataForm
   */
  public async addListItem(listName: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Check that the destination list is ext in the dataForm, otherwise initialize it
    if (!this.dataForm[listName]) {
      this.dataForm[listName] = [];
    }

    let contained = false;

    // Check that the element to be added does not exist in the destination list, otherwise it triggers an alert
    if (this.dataFormAux.id) {
      for (const element of this.dataForm[listName]) {
        if (element.id === this.dataFormAux.id) {
          contained = true;
          break;
        }
      }
    }

    //check that the element does not exist in the table

    if (!contained) {
      // add the data item to the list and table
      this.dataForm[listName].push(this.dataFormAux);
      this.dataForm.formAux = null;
      this.dataFormAux = {};
    } else {
      this.alertService.createAlert(
        this.alertService.jsonData['alert']['repeatedElement'],
        'warning',
        false
      );
    }
  }

  //Delete elements from the list of the table
  public async deleteListItem(item: any, listName: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    const index = this.dataForm[listName].indexOf(item);
    if (index >= 0) {
      this.alertService
        .createAlert(
          this.alertService.jsonData['alert']['deleteElement'],
          'warning',
          false
        )
        .then((result) => {
          this.dataForm[listName].splice(index, 1);
        });
    }
  }

  public async addListItemLawyer(listName: string): Promise<void> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Check that the destination list is ext in the dataForm, otherwise initialize it
    if (!this.dataForm[listName]) {
      this.dataForm[listName] = [];
    }

    // add the data item to the list and table
    this.dataForm[listName].push(this.dataFormAux);
    this.dataForm.formAux = null;
    this.dataFormAux = {};
  }

  /**
   * Creates a new record via a POST request.
   *
   * @param endPoint - The URL of the API endpoint.
   * @param dataForm -  The name of the modal associated with this create operation
   * @returns A Promise containing the updated record or an error message.
   */
  public async generateLoanSchedule(
    dataForm: any
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    // Create a Loading alert to indicate that a process is loading
    //await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    let data = await this.makeFormData(dataForm);

    try {
      const response = await firstValueFrom(
        this.http
          .post<any>(environment.apiUrl2 + '/generateLoanSchedule', data, {headers})
          .pipe(
            catchError((error) => {
              this.userInfoService.handleError(error);
              throw error;
            })
          )
      );
      this.schedule = response;
      // Validar y mostrar si llega el campo totalInterestCharged
      if (response.totalInterestCharged !== undefined) {
        //console.log('totalInterestCharged recibido en generateLoanSchedule:', response.totalInterestCharged);
      } else {
        //console.warn('No se recibió el campo totalInterestCharged en generateLoanSchedule:', response);
      }
      return response;
    } catch (error: any) {
      if (error && error.message && error.type_message) {
        this.alertService.createAlert(error.message, error.type_message, false);
      }
      throw error;
    }
  }

  public async remove(
    endPoint: string,
    item: any,
    dataForm: any
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    // Create a Loading alert to indicate that a process is loading
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    this.dataForm = item;

    let data = await this.makeFormData(dataForm);

    this.http
      .post<any>(environment.apiUrl + endPoint, data, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        console.log(data);
        if (data.data !== null) {
          this.alertService
            .createAlert(data.message, data.type_message, false)
            .then((result) => {
              window.location.reload();
            });
        } else {
          this.alertService.createAlert(data.message, data.type_message, false);
          window.location.reload();
        }
      });
  }

  public async toBase64(fileBase64: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(fileBase64);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  }

  /**
   * Creates a new record via a POST request.
   *
   * @param endPoint - The URL of the API endpoint.
   * @param formData -  The name of the modal associated with this create operation
   * @returns A Promise containing the updated record or an error message.
   */

  public async changePassword(endPoint: string, dataForm: any): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);

    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    let data = await this.makeFormData(dataForm);

    this.http
      .put<any>(environment.apiUrl + endPoint, data, {headers})
      .pipe(
        //Catch the error to generate a new action
        catchError((error) => this.userInfoService.handleError(error))
      )
      .subscribe((data) => {
        if (data.type_message === 'success') {
          this.alertService
            .createAlert(data.message, data.type_message, false)
            .then((result) => {
              //this.dataFormTemp = {};
              this.userInfoService.logout();
            });
        } else {
          this.alertService
            .createAlert(data.message, data.type_message, false)
            .then((result) => {
              //location.reload();
            });
        }
      });
  }

  /**
   * Handles the response from Khipu after a payment is processed.
   *
   * @param endPoint - The URL of the API endpoint to handle the Khipu response.
   * @param khipuData - The data received from Khipu (e.g., payment status, transaction ID).
   * @returns A Promise indicating success or failure of the operation.
   */
  public async createKhipuPayment(
    endPoint: string,
    khipuData: any
  ): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
      'Content-Type': 'application/json',
    };

    try {
      const data = await firstValueFrom(
        this.http
          .post<any>(environment.apiUrl + endPoint, khipuData, {headers})
          .pipe(
            catchError((error) => {
              this.userInfoService.handleError(error);
              return throwError(() => error);
            })
          )
      );

      this.alertService.closeAlertLoading();

      if (data?.payment_url) {
        this.alertService.createAlert(
          'Redirigiendo a Khipu...',
          'success',
          false
        );
        return data;
      } else {
        this.alertService.createAlert(
          'No se recibió una URL de pago de Khipu',
          'error',
          false
        );
        throw new Error('Respuesta inesperada de Khipu');
      }
    } catch (error) {
      this.alertService.closeAlertLoading();
      console.error('Error al procesar la respuesta de Khipu:', error);
      this.alertService.createAlert(
        'Error al procesar la respuesta de Khipu',
        'error',
        false
      );
      throw error;
    }
  }

  /**
   * Copia los datos del usuario desde UserInfoService a dataForm para los tabs de perfil
   */
  public setUserDataFromUserInfo() {
    const user = this.userInfoService;
    const userInfo = user.userInfo || {};
    this.dataForm = {
      // Datos personales
      nationality: user.nationality || null,
      occupation: user.occupation || null,
      maritalStatusId: user.maritalStatusId || null,
      firstName: user.firstName || userInfo.given_name || '',
      lastname: user.lastName || userInfo.family_name || '',
      userName: user.userName || userInfo.name || '',
      email: user.email || userInfo.email || '',
      phoneNumber: user.phone || userInfo.phone || '',
      country: user.country || userInfo.country || '',
      documentNumber: user.documentNumber || user.fineractId || userInfo.fineract_id || '',
      dateOfBirth: userInfo.birthdate || '',
      countryId: '', // No disponible
      // Dirección
      postalCode: userInfo.postalCode || '',
      street: userInfo.streetAddress || '',
      number: '', // No disponible
      commune: userInfo.locality || '',
      city: userInfo.locality || '',
      region: userInfo.region || '',
      // Datos bancarios (no disponible en el JSON, se deja vacío)
      bankId: '',
      typeAccountId: '',
      accountNumber: '',
      // AGREGADO: Tipo de cliente
      tipoCliente: user.tipoCliente || userInfo.tipoCliente || '',
    };
  }


  /**
   * Actualiza la información de perfil del usuario usando el endpoint /update-profiling-information
   */
  public async updateProfilingInformation(data: {
    clientId: number,
    countryId?: number,
    invQualified?: boolean,
    maritalStatusId?: number,
    occupation?: string,
    address?: string,
    pXposed?: boolean
  }): Promise<any> {
    await this.alertService.loadJsonData(this.translate.currentLang);
    await this.alertService.createAlertLoading();

    const token = await this.userInfoService.getToken();
    const headers = {
      'Accept-Language': <string>this.translate.currentLang,
      Authorization: token,
      country: this.userInfoService.country,
    };

    let params = new HttpParams()
    if (data.countryId !== undefined) params = params.set('countryId', data.countryId.toString());
    if (data.invQualified !== undefined) params = params.set('invQualified', data.invQualified.toString());
    if (data.maritalStatusId !== undefined) params = params.set('maritalStatusId', data.maritalStatusId.toString());
    if (data.occupation !== undefined) params = params.set('occupation', data.occupation);
    if (data.address !== undefined) params = params.set('address', data.address);
    if (data.pXposed !== undefined) params = params.set('pXposed', data.pXposed.toString());

    return this.http
      .put<any>(environment.apiUrl + '/update-profiling-information', null, {headers, params})
      .pipe(
        catchError((error) => this.userInfoService.handleError(error))
      )
      .toPromise()
      .finally(() => this.alertService.closeAlertLoading());
  }

  get fineractId(): number | null {
    const fineractId = this.userInfoService.fineractId || localStorage.getItem('fineractId');
    if (fineractId) {
      return Number(fineractId);

    } else {
      return null;

    }
  }

  // En tu CRUDService
  public getDataFormAuxWithTipoCliente(): any {
    return {
      ...this.dataFormAux,
      tipoCliente: this.userInfoService.tipoCliente || this.userInfoService.userInfo?.tipoCliente || 'retail'
    };
  }

}
