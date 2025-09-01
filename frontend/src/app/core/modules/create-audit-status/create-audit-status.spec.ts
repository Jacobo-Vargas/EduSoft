import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAuditStatus } from './create-audit-status';

describe('CreateAuditStatus', () => {
  let component: CreateAuditStatus;
  let fixture: ComponentFixture<CreateAuditStatus>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateAuditStatus]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateAuditStatus);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
