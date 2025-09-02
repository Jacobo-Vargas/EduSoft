import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateStatusCourse } from './create-status-course';

describe('CreateStatusCourse', () => {
  let component: CreateStatusCourse;
  let fixture: ComponentFixture<CreateStatusCourse>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateStatusCourse]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateStatusCourse);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
