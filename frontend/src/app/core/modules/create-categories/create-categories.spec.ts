import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCategories } from './create-categories';

describe('CreateCategories', () => {
  let component: CreateCategories;
  let fixture: ComponentFixture<CreateCategories>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateCategories]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateCategories);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
