import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarStudent } from './navbar-student';

describe('NavbarStudent', () => {
  let component: NavbarStudent;
  let fixture: ComponentFixture<NavbarStudent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarStudent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarStudent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
