import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendEmail } from './send-code-email';

describe('SendEmail', () => {
  let component: SendEmail;
  let fixture: ComponentFixture<SendEmail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SendEmail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SendEmail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
