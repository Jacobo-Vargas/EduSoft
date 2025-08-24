import { TestBed } from '@angular/core/testing';

import { NgSelectFactoryService } from './ng-select-factory.service';

describe('NgSelectFactoryService', () => {
  let service: NgSelectFactoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NgSelectFactoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
