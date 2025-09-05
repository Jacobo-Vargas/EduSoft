import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';
import { AlertService } from './alert.service';

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

  public async toBase64(fileBase64: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(fileBase64);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  }

}
