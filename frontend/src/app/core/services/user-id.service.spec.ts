import { TestBed } from '@angular/core/testing';
import { UserIdService } from './user-id.service';

describe('UserIdService', () => {
  let service: UserIdService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserIdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set and get fineractId', (done) => {
    service.setFineractId(123);
    service.getFineractId().subscribe(id => {
      expect(id).toBe(123);
      done();
    });
  });

  it('should return current fineractId', () => {
    service.setFineractId(456);
    expect(service.getCurrentFineractId()).toBe(456);
  });
});
